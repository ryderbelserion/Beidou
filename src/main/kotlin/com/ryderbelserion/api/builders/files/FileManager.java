package com.ryderbelserion.api.builders.files;

import com.ryderbelserion.api.DiscordPlugin;
import com.ryderbelserion.api.builders.files.enums.FileAction;
import com.ryderbelserion.api.builders.files.enums.FileType;
import com.ryderbelserion.api.builders.files.interfaces.ICustomFile;
import com.ryderbelserion.api.builders.files.interfaces.IFileManager;
import com.ryderbelserion.api.builders.files.types.JsonCustomFile;
import com.ryderbelserion.api.builders.files.types.YamlCustomFile;
import com.ryderbelserion.api.exceptions.FileException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileManager extends IFileManager<FileManager> {

    private final DiscordPlugin plugin;
    private final Logger logger;
    private final Path dataPath;

    public FileManager(@NotNull final DiscordPlugin plugin) {
        this.dataPath = plugin.getDirectory();
        this.logger = plugin.getLogger();
        this.plugin = plugin;
    }

    protected final Map<Path, ICustomFile<?, ?, ?, ?>> files = new HashMap<>();

    @Override
    public @NotNull FileManager addFolder(@NotNull final Path folder, @NotNull final FileType fileType, @NotNull final Consumer<ICustomFile<?, ?, ?, ?>> consumer) {
        extractFolder(folder.getFileName().toString(), folder.getParent());

        for (final Path path : getFiles(folder, ".yml", 1)) {
            addFile(path, fileType, consumer);
        }

        return this;
    }

    @Override
    public @NotNull FileManager addFile(@NotNull final Path path, @NotNull final FileType fileType, @NotNull final Consumer<ICustomFile<?, ?, ?, ?>> consumer) {
        if (this.files.containsKey(path)) {
            final ICustomFile<?, ?, ?, ?> customFile = this.files.get(path);

            consumer.accept(customFile);

            if (!customFile.hasAction(FileAction.FILE_ALREADY_RELOADED)) {
                customFile.load();

                return this;
            }

            return this;
        }

        ICustomFile<?, ?, ?, ?> customFile = null;

        switch (fileType) {
            case YAML -> customFile = buildYamlFile(path, consumer::accept);
            case JSON -> customFile = buildJsonFile(path, consumer::accept);
        }

        if (customFile == null) return this;

        this.files.putIfAbsent(path, customFile);

        return this;
    }

    @Override
    public @NotNull FileManager removeFile(@NotNull final Path path) {
        final Optional<ICustomFile<?, ?, ?, ?>> variable = getFile(path);

        if (variable.isEmpty()) {
            return this;
        }

        this.files.remove(path);

        return this;
    }

    @Override
    public @NotNull FileManager purge() {
        final Map<Path, ICustomFile<?, ?, ?, ?>> files = new HashMap<>(this.files);

        for (final Map.Entry<Path, ICustomFile<?, ?, ?, ?>> entry : files.entrySet()) {
            removeFile(entry.getKey());
        }

        return this;
    }

    public @NotNull FileManager addFile(@NotNull final Path path, @NotNull final ICustomFile<?, ?, ?, ?> customFile) {
        this.files.putIfAbsent(path, customFile);

        return this;
    }

    @Override
    public @NotNull YamlCustomFile buildYamlFile(@NotNull final Path path, @NotNull final Consumer<YamlCustomFile> consumer) {
        return new YamlCustomFile(this, path, consumer).load();
    }

    @Override
    public @NotNull JsonCustomFile buildJsonFile(@NotNull final Path path, @NotNull final Consumer<JsonCustomFile> consumer) {
        return new JsonCustomFile(this, path, consumer).load();
    }

    @Override
    public @NotNull FileManager reloadFile(@NotNull final Path path) {
        final Optional<ICustomFile<?, ?, ?, ?>> customFile = getFile(path);

        if (customFile.isEmpty()) return this;

        customFile.get().load();

        return this;
    }

    @Override
    public @NotNull FileManager saveFile(@NotNull final Path path) {
        final Optional<ICustomFile<?, ?, ?, ?>> customFile = getFile(path);

        if (customFile.isEmpty()) return this;

        customFile.get().save();

        return this;
    }

    @Override
    public @NotNull Optional<ICustomFile<?, ?, ?, ?>> getFile(@NotNull final Path path) {
        return Optional.ofNullable(this.files.get(path));
    }

    @Override
    public @NotNull FileManager compressFolder(@NotNull final Path path, @NotNull final String content) {
        if (!Files.exists(path)) return this;
        if (!Files.isDirectory(path)) return this;

        final Path target = this.dataPath.resolve(asString(path, content));

        try (final ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(target)); final Stream<Path> values = Files.walk(path)) {
            final List<Path> entries = values.filter(key -> !Files.isDirectory(key)).toList();

            for (final Path entry : entries) {
                if (Files.size(entry) <= 0L) continue;

                final ZipEntry zipEntry = new ZipEntry(entry.toString());

                output.putNextEntry(zipEntry);

                Files.copy(entry, output);

                output.closeEntry();
            }
        } catch (final IOException exception) {
            throw new FileException("Failed to compress folder %s".formatted(path), exception);
        }

        return this;
    }

    @Override
    public @NotNull FileManager compressFile(@NotNull final Path path, @Nullable final Path folder, @NotNull final String content) {
        if (!Files.exists(path)) return this;

        long size;

        try {
            size = Files.size(path);
        } catch (final Exception exception) {
            throw new FileException("Failed to calculate file size for %s".formatted(path), exception);
        }

        if (size <= 0L) return this;

        final String builder = asString(path, content);

        final Path target = folder == null ? this.dataPath : folder.resolve(builder);

        try (final ZipOutputStream output = new ZipOutputStream(Files.newOutputStream(target))) {
            final ZipEntry entry = new ZipEntry(path.getFileName().toString());

            output.putNextEntry(entry);

            Files.copy(path, output);

            output.closeEntry();
        } catch (final Exception exception) {
            //throw new FileException("Failed to compress %s".formatted(path), exception);
        }

        return this;
    }

    @Override
    public @NotNull FileManager writeFile(@NotNull final Path path, @NotNull final String content) {
        try {
            Files.writeString(path, content, StandardOpenOption.APPEND);
        } catch (final IOException exception) {
            //throw new FileException(String.format("Failed to write %s to %s", content, path), exception);
        }

        return this;
    }

    @Override
    public final boolean hasFile(@NotNull final Path path) {
        return this.files.containsKey(path);
    }

    public @NotNull final Map<Path, ICustomFile<?, ?, ?, ?>> getFiles() {
        return Collections.unmodifiableMap(this.files);
    }

    @Override
    public @NotNull final FileManager refresh(final boolean save) { // save or reload all existing files
        if (this.files.isEmpty()) return this;

        final List<Path> keys = new ArrayList<>();

        for (final Map.Entry<Path, ICustomFile<?, ?, ?, ?>> file : this.files.entrySet()) {
            final ICustomFile<?, ?, ?, ?> value = file.getValue();

            if (value == null) continue;

            final Path path = value.getPath();

            if (!Files.exists(path)) {
                keys.add(file.getKey());

                continue;
            }

            if (save) {
                value.save(); // save the config
            } else {
                value.load(); // load the config
            }
        }

        if (!keys.isEmpty()) keys.forEach(this.files::remove);

        return this;
    }

    @Override
    public void extractFolder(@NotNull final String folder, @NotNull final Path output) {
        final Path path = output.resolve(folder);

        if (Files.exists(path)) { // do not extract if path exists.
            this.logger.info("Cannot extract folder {} to {}, because it already exists @ {}.", folder, output, path);

            return;
        }

        if (Files.notExists(path)) { // create folder
            try {
                Files.createDirectories(path);
            } catch (final Exception exception) {
                throw new FileException("Failed to create %s".formatted(path), exception);
            }
        }

        try (final JarFile jarFile = new JarFile(Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            final Set<JarEntry> entries = jarFile.stream().filter(entry -> !entry.getName().endsWith(".class"))
                    .filter(entry -> !entry.getName().startsWith("META-INF"))
                    .filter(entry -> !entry.isDirectory())
                    .filter(entry -> entry.getName().startsWith(folder))
                    .collect(Collectors.toSet());

            entries.forEach(entry -> {
                final Path target = output.resolve(entry.getName());
                final Path parent = target.getParent();

                if (!Files.exists(parent)) {
                    try {
                        Files.createDirectories(parent);
                    } catch (final IOException exception) {
                        //throw new FileException("Failed to create %s".formatted(parent), exception);
                    }
                }

                if (Files.notExists(target)) {
                    try (final InputStream stream = jarFile.getInputStream(entry)) {
                        Files.copy(stream, target);
                    } catch (final IOException exception) {
                        throw new FileException("Failed to copy %s to %s".formatted(target, parent), exception);
                    }
                }
            });
        } catch (final IOException | URISyntaxException exception) {
            throw new FileException("Failed to extract folder %s".formatted(path), exception);
        }
    }

    @Override
    public void extractFile(@NotNull final Path path) {
        if (Files.exists(path)) { // Do not extract if path exists.
            this.logger.info("Cannot extract {} to {}, because it already exists.", path.getFileName().toString(), path);

            return;
        }

        final String fileName = path.getFileName().toString();

        try (final JarFile jarFile = new JarFile(Path.of(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            final Set<JarEntry> entries = jarFile.stream().filter(entry -> !entry.getName().endsWith(".class"))
                    .filter(entry -> !entry.getName().startsWith("META-INF"))
                    .filter(entry -> !entry.isDirectory())
                    .filter(entry -> entry.getName().equalsIgnoreCase(fileName))
                    .collect(Collectors.toSet());

            entries.forEach(entry -> {
                if (Files.notExists(path)) {
                    try (final InputStream stream = jarFile.getInputStream(entry)) {
                        Files.copy(stream, path);
                    } catch (final IOException exception) {
                        throw new FileException("Failed to copy %s to %s".formatted(entry.getName(), path), exception);
                    }
                }
            });
        } catch (final IOException | URISyntaxException exception) {
            throw new FileException("Failed to extract file %s".formatted(path), exception);
        }
    }

    @Override
    public final List<String> getFileNames(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final int depth, final boolean removeExtension) {
        final List<Path> files = getFiles(folder.isEmpty() ? path : path.resolve(folder), List.of(extension), depth);

        final List<String> names = new ArrayList<>();

        for (final Path key : files) {
            final String fileName = key.getFileName().toString();

            if (!fileName.endsWith(extension)) continue;

            names.add(removeExtension ? fileName.replace(extension, "") : fileName);
        }

        return names;
    }

    @Override
    public List<String> getFileNames(@NotNull final String folder, @NotNull final Path path, @NotNull final String extension, final boolean removeExtension) {
        return getFileNames(folder, path, extension, 1, removeExtension);
    }

    @Override
    public List<Path> getFiles(@NotNull final Path path, @NotNull final List<String> extensions, final int depth) {
        final List<Path> files = new ArrayList<>();

        if (Files.notExists(path) || !Files.isDirectory(path)) return new ArrayList<>();

        try {
            Files.walkFileTree(path, new HashSet<>(), Math.max(depth, 1), new SimpleFileVisitor<>() {
                @Override
                public @NotNull FileVisitResult visitFile(@NotNull final Path path, @NotNull final BasicFileAttributes attributes) {
                    final String fileName = path.getFileName().toString();

                    extensions.forEach(extension -> {
                        if (fileName.endsWith(extension)) files.add(path);
                    });

                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (final IOException exception) {
            throw new FileException("Failed to get a list of files", exception);
        }

        return files;
    }

    @Override
    public List<Path> getFiles(@NotNull final Path path, @NotNull final String extension, final int depth) {
        return getFiles(path, List.of(extension), depth);
    }

    @Override
    public int getDirectorySize(@NotNull final Path path, @NotNull final String extension) {
        return getFiles(path, extension, 1).size();
    }
}