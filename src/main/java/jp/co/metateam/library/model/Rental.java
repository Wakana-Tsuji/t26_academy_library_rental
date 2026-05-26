package jp.co.metateam.library.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rental_manage")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 社員番号
    private String employeeId;

    // 貸出予定日
    private LocalDate expectedRentalOn;

    // 返却予定日
    private LocalDate expectedReturnOn;

    // 在庫管理番号
    private String stockId;

    // 貸出ステータス
    private Integer status;

    // ===== getter / setter =====

    public Long getId() {
        return id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDate getExpectedRentalOn() {
        return expectedRentalOn;
    }

    public LocalDate getExpectedReturnOn() {
        return expectedReturnOn;
    }

    public String getStockId() {
        return stockId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setExpectedRentalOn(LocalDate expectedRentalOn) {
        this.expectedRentalOn = expectedRentalOn;
    }

    public void setExpectedReturnOn(LocalDate expectedReturnOn) {
        this.expectedReturnOn = expectedReturnOn;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
