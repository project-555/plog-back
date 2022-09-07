package com.plogcareers.backend.blog.domain.entity;

import javax.persistence.*;

@Entity
@Table(schema = "plog_blog", name = "posting")
public class Posting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
}
