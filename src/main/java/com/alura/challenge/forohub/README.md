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

Contraseñas:

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
"username": "usuario_prueba",
"password": "contraseña123"
},
{
"username": "usuario.prueba",
"password": "clave123"
},
{
"username": "mariana.lois",
"password": "clavesecreta123"
}
]

set JWT_SECRET_FOROHUB=clavesecreta123
mvn spring-boot:run

