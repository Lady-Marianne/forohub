package com.alura.challenge.forohub.domain.course;

import com.alura.challenge.forohub.domain.answer.DataRegisterAnswer;
import com.alura.challenge.forohub.domain.topic.Topic;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "Course")
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "courseId")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Topic> topics;

    public Course(DataRegisterCourse dataRegisterCourse) {
     this.name = dataRegisterCourse.name();
     this.category = dataRegisterCourse.category();
    }
}
