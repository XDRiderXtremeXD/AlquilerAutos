 package proyectosCibertec.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.github.cdimascio.dotenv.Dotenv;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

@SpringBootApplication
public class AlquilerAutoProjectApplication {

	public static void main(String[] args) {

		// Solo cargar .env si NO estás en producción (ej. Railway)
		if (System.getenv("RAILWAY_ENVIRONMENT_NAME") == null) {
			Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()  // <- evita errores si .env no existe
				.filename(".env")
				.load();

			// Puedes setear propiedades si lo necesitas (opcional)
			System.setProperty("SPRING_DATASOURCE_URL", dotenv.get("SPRING_DATASOURCE_URL"));
			System.setProperty("SPRING_DATASOURCE_USERNAME", dotenv.get("SPRING_DATASOURCE_USERNAME"));
			System.setProperty("SPRING_DATASOURCE_PASSWORD", dotenv.get("SPRING_DATASOURCE_PASSWORD"));

			System.setProperty("CLOUDINARY_CLOUD_NAME", dotenv.get("CLOUDINARY_CLOUD_NAME"));
			System.setProperty("CLOUDINARY_API_KEY", dotenv.get("CLOUDINARY_API_KEY"));
			System.setProperty("CLOUDINARY_API_SECRET", dotenv.get("CLOUDINARY_API_SECRET"));
		}
		else {
			
			System.out.println("=== TODAS LAS VARIABLES DE ENTORNO ===");
			System.getenv().forEach((key, value) -> {
			    System.out.println(key + "=" + value);
			});
			
		}
		
		System.out.println("=== DB URL ===");
		System.out.println(System.getenv("SPRING_DATASOURCE_URL"));
		System.out.println("=== USER ===");
		System.out.println(System.getenv("SPRING_DATASOURCE_USERNAME"));
		System.out.println("=== PWD ===");
		System.out.println(System.getenv("SPRING_DATASOURCE_PASSWORD"));

		SpringApplication.run(AlquilerAutoProjectApplication.class, args);
	}

	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}
}
