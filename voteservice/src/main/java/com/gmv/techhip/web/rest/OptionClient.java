package com.gmv.techhip.web.rest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class OptionClient extends AbstractMicroserviceClient<Option> {
	
    public OptionClient() {
        super("OptionService");
    }

    /**
     * A reduced version of a Bar, with ignoring id on Jacksons serialization
     */
    @JsonIgnoreProperties({"id"})
    static class NewOption extends Option {
        public NewOption(Option base) {
            this.setOption(base.getOption());
        }
    }


    @Override
    public Collection<Option> findAll() {
        return Arrays.asList(restTemplate.getForEntity(getUrl("options"), Option[].class).getBody());
    }


	@Override
	public Option getOne(long id) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Option create(Option object) throws JsonProcessingException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Option update(Option object) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void delete(long id) {
		// TODO Auto-generated method stub
		
	}
}