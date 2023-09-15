package dev.courses.springdemo.revision;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "revinfo")
@RevisionEntity(UserAuditListener.class)
public class RevEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @JoinColumn(name = "rev")
    private int rev;

    @RevisionTimestamp
    @Column(name = "revtstmp")
    private long revtstmp;

    @Column(name = "author")
    private String author;

}
