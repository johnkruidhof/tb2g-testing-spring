package org.springframework.samples.petclinic.sfg;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("yanny")
@Primary
@Component
public class YannyWordProducer implements WordProducer {

    public static final String YANNY = "Yanny";

    @Override
    public String getWord() {
        return YANNY;
    }
}
