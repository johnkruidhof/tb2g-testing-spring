package org.springframework.samples.petclinic.sfg.junit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.sfg.BaseConfig;
import org.springframework.samples.petclinic.sfg.HearingInterpreter;
import org.springframework.samples.petclinic.sfg.YannyConfig;
import org.springframework.samples.petclinic.sfg.YannyWordProducer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("base-test")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BaseConfig.class, YannyConfig.class})
public class HearingInterpreterYannyTest {

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    public void whatIheard() {
        String word = hearingInterpreter.whatIheard();

        Assert.assertEquals(YannyWordProducer.YANNY, word);
    }
}