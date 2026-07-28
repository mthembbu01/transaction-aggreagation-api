package za.co.capitec.accounts.configurations.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

/**
 * Configuration class for setting up and customizing the {@link ModelMapper} bean.
 * <p>
 * This class defines a Spring-managed {@link ModelMapper} bean and configures it with
 * specific settings to facilitate object mapping in the application.
 * <p>
 * The customization includes:
 * - Enabling skipping of null values during mapping.
 * - Enabling field matching and configuring the field access level to {@link org.modelmapper.config.Configuration.AccessLevel#PRIVATE}.
 * - Setting the matching strategy to {@link MatchingStrategies#STRICT}.
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        //-- Create ModelMapper instance
        ModelMapper modelMapper = new ModelMapper();
        //-- Configure ModelMapper
        modelMapper
                .getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        //-- Return ModelMapper instance
        return modelMapper;
    }

}
