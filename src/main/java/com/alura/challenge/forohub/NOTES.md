Solucionar el siguiente error:

2024-12-07T10:00:19.070-03:00  WARN 20284 --- [forohub] [nio-8080-exec-1]
ration$PageModule$WarningLoggingModifier : Serializing PageImpl instances as-is is not supported,
meaning that there is no guarantee about the stability of the resulting JSON structure!
For a stable JSON structure, please use Spring Data's PagedModel (globally via
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO))
or Spring HATEOAS and Spring Data's PagedResourcesAssembler as documented in
https://docs.spring.io/spring-data/commons/reference/repositories/core-extensions.html#core.web.pageables.

Posible solución:

import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/topics")
public class TopicController {

    private final TopicRepository topicRepository;
    private final PagedResourcesAssembler<DataResponseTopic> pagedResourcesAssembler;

    public TopicController(TopicRepository topicRepository, PagedResourcesAssembler<DataResponseTopic> pagedResourcesAssembler) {
        this.topicRepository = topicRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<DataResponseTopic>> listActiveTopics(@PageableDefault(size = 10) Pageable pageable) {
        // Consulta los tópicos activos
        Page<Topic> topics = topicRepository.findByStatus(Status.ACTIVE, pageable);

        // Mapea la página a un DTO
        Page<DataResponseTopic> response = topics.map(DataResponseTopic::new);

        // Convierte la página a un PagedModel con enlaces HATEOAS
        PagedModel<DataResponseTopic> pagedModel = pagedResourcesAssembler.toModel(
            response,
            topic -> WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TopicController.class)
                    .listActiveTopics(pageable) // Enlace al mismo método
            ).withSelfRel()
        );

        // Retorna la respuesta con el modelo paginado
        return ResponseEntity.ok(pagedModel);
    }
}

Usuarios y contraseñas:

[
{
"username": "Juan Perez",
"password": "123456"
},
{
"username": "Carlos Lopez",
"password": "qwerty789"
},
{
"username": "Maria Garcia",
"password": "password123"
},
{
"username": "Olga Rodriguez",
"password": "contrasenia123"
},
{
"username": "Alejandra Vazquez",
"password": "clave123"
},
{
"username": "Mariana Lois",
"password": "clavesecreta123"
}

{
"username": "Pablo Levi",
"password": "999999"
}
]

set JWT_SECRET_FOROHUB=clavesecreta123
mvn spring-boot:run

SECURITY CONFIGURATION:

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/login")
                        .permitAll()
                       .requestMatchers(HttpMethod.DELETE, "/topics").hasRole("ADMIN")
                       .requestMatchers("/error")
                       .permitAll()
/                     .requestMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**")
.permitAll()
.anyRequest()
.authenticated())
.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
return httpSecurity.build();
}

SECURITY FILTER:

        String requestPath = request.getRequestURI();
        if (requestPath.equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

Para saber más: control de acceso a anotaciones

Otra forma de restringir el acceso a ciertas funciones, según el perfil del usuario,
es usar una función de Spring Security conocida como Method Security,
que funciona con el uso de anotaciones en los métodos:

@GetMapping("/{id}")
@Secured("ROLE_ADMIN")
public ResponseEntity detallar(@PathVariable Long id) {
var medico = repository.getReferenceById(id);
return ResponseEntity.ok(new DatosDetalladoMedico(medico));
}Copia el código
En el ejemplo de código anterior, el método se anotó con @Secured("ROLE_ADMIN"), 
de modo que sólo los usuarios con el rol ADMIN pueden activar solicitudes para detallar a un médico.
La anotación @Secured se puede agregar en métodos individuales o incluso en la clase,
lo que sería el equivalente a agregarla en todos los métodos.

¡Atención! Por defecto esta característica está deshabilitada en Spring Security,
y para usarla debemos agregar la siguiente anotación en la clase Securityconfigurations del proyecto:

@EnableMethodSecurity(securedEnabled = true)Copia el código
Puede obtener más detalles sobre la función de seguridad del método en la documentación de
Spring Security, disponible en:

Method Security