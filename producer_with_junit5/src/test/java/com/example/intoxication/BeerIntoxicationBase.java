package com.example.intoxication;

//remove::start[]

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import static com.example.intoxication.DrunkLevel.DRUNK;
import static com.example.intoxication.DrunkLevel.SOBER;
import static com.example.intoxication.DrunkLevel.TIPSY;
import static com.example.intoxication.DrunkLevel.WASTED;

//remove::end[]

/**
 * Tests for the scenario based stub
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BeerIntoxicationBase.Config.class)
public abstract class BeerIntoxicationBase {

	@Autowired
	WebApplicationContext webApplicationContext;

	@BeforeEach
	public void setup() {
		//remove::start[]
		RestAssuredMockMvc.webAppContextSetup(this.webApplicationContext);
		//remove::end[]
	}

	@Configuration
	@EnableAutoConfiguration
	static class Config {

		@Bean
		BeerServingController controller() {
			return new BeerServingController(responseProvider());
		}

		@Bean
		ResponseProvider responseProvider() {
			return new MockResponseProvider();
		}
	}

	//tag::mock[]
	static class MockResponseProvider implements ResponseProvider {

		private DrunkLevel previous = SOBER;
		private DrunkLevel current = SOBER;

		@Override
		public Response thereYouGo(Customer personToCheck) {
			//remove::start[]
			if ("marcin".equals(personToCheck.name)) {
				switch (this.current) {
				case SOBER:
					this.current = TIPSY;
					this.previous = SOBER;
					break;
				case TIPSY:
					this.current = DRUNK;
					this.previous = TIPSY;
					break;
				case DRUNK:
					this.current = WASTED;
					this.previous = DRUNK;
					break;
				case WASTED:
					throw new UnsupportedOperationException("You can't handle it");
				}
			}
			//remove::end[]
			return new Response(this.previous, this.current);
		}
	}
	//end::mock[]
}
