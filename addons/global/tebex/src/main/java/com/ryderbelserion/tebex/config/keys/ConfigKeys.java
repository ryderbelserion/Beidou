package com.ryderbelserion.tebex.config.keys;

import com.ryderbelserion.fusion.config.annotations.Comment;
import com.ryderbelserion.fusion.config.properties.builders.CommentsBuilder;
import com.ryderbelserion.fusion.config.properties.interfaces.IPropertyHolder;
import com.ryderbelserion.fusion.config.properties.objects.interfaces.Property;
import org.jspecify.annotations.NonNull;
import static com.ryderbelserion.fusion.config.properties.PropertyBuilder.newProperty;

public class ConfigKeys implements IPropertyHolder {

    @Override
    public void registerComments(@NonNull final CommentsBuilder builder) {
        builder.setComment("All settings related to the Tebex integration.", "settings", "private-key");
    }

    @Comment("The private key found in Integrations -> API Keys")
    public static final Property<String> tebex_private_key = newProperty("", "settings", "private-key");

}