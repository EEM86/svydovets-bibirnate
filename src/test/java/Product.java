import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.svydovets.bibirnate.annotation.Column;
import com.svydovets.bibirnate.annotation.Entity;
import com.svydovets.bibirnate.annotation.Id;
import com.svydovets.bibirnate.annotation.Table;

import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    private int id;

    private String name;

    private BigDecimal price;

//    @Column(name = "created_at")
//    private Date date;

//    @Column(name = "created_at")
//    private LocalDateTime date;

    @Column(name = "created_at")
    private Timestamp date;
}
