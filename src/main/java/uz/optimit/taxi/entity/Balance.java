//package uz.optimit.taxi.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import uz.optimit.taxi.entity.User;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Entity
//public class Balance {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    private double balance;
//
//    private LocalDateTime createdDate;
//
//    @OneToOne
//    private User user;
//}
