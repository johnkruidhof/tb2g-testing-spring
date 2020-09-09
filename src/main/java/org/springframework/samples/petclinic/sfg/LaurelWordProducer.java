package org.springframework.samples.petclinic.sfg;

import org.springframework.stereotype.Component;

@Component
public class LaurelWordProducer implements WordProducer {

    public static final String LAUREL = "Laurel";

    @Override
    public String getWord() {
        return LAUREL;
    }
}
