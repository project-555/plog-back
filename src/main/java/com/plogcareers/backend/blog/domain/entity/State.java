package com.plogcareers.backend.blog.domain.entity;

import com.plogcareers.backend.blog.domain.model.StateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "state", schema = "plog_blog")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class State {
    @Transient
    public final static Long PUBLIC = 1L;

    @Transient
    public final static Long PRIVATE = 2L;
    
    @Transient
    public final static Long KEEP = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "state_name")
    String stateName;

    public StateDTO toStateDTO() {
        return new StateDTO(id, stateName);
    }
}
