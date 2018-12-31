package springMVC.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

	@Bean(initMethod = "init", destroyMethod = "cleanup")
	public Foo foo() {
		return new Foo();
	}

	public class Foo {
		public void init() {
			// initialization logic
		}

		public void cleanup() {
			// destruction logic
		}
	}
}
