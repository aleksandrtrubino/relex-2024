package ru.trubino.farm;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
		info = @Info(
				title = "Web-приложение 'Журнал сельскохозяйственного производства'",
				description = "В данном приложении функционал меняется в зависимости от привилегий пользователя." +
						"Далее в документации будут использоваться следующие термины:\n" +
						"Владелец - пользователь с привилегией ROLE_OWNER\n" +
						"Работник - пользователь с привилегией ROLE_EMPLOYEE\n" +
						"Владелец может:\n" +
						"- ",
				version = "1.0.0",
				contact = @Contact(
						name = "Александр Трубино",
						email = "trubino2003@gmail.com",
						url = "https://github.com/aleksandrtrubino"
				)
		)
)
@EnableScheduling
@SpringBootApplication
public class FarmApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmApplication.class, args);
	}

}
