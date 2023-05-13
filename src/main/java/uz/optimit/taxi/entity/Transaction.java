//package uz.optimit.taxi.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import uz.optimit.taxi.entity.Enum.TransactionType;
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
//public class Transaction {
//
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private UUID id;
//
//    @Enumerated(EnumType.STRING)
//    private TransactionType transactionType;
//
//    @Column(nullable = false)
//    private double sum;
//
//    private UUID userId;
//
//    private LocalDateTime createdTime;
//}
