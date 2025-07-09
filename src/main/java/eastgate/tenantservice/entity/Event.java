package eastgate.tenantservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "event")
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @SequenceGenerator(name = "event_seq", sequenceName = "event_seq", allocationSize = 5)
    private Long id;



    @Column(name = "time_stamp")
    private Timestamp timestamp;

    @Column(name = "event_number")
    private Integer eventNumber;

    public Event(Timestamp timestamp, Integer eventNumber) {
        this.timestamp = timestamp;
        this.eventNumber = eventNumber;
    }

    public Event() {

    }
}