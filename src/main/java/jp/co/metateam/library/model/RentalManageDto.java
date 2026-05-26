package jp.co.metateam.library.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RentalManageDto {
    // 社員番号
    @NotBlank(message = "社員番号は必須です。")
    private String employeeId;

    // 貸出予定日
    @NotNull(message = "貸出予定日は必須です。")
    private LocalDate expectedRentalOn;

    // 返却予定日
    @NotNull(message = "返却予定日は必須です。")
    private LocalDate expectedReturnOn;

    // 在庫管理番号
    @NotBlank(message = "在庫管理番号は必須です。")
    private String stockId;

    // 貸出ステータス
    @NotNull(message = "ステータスは必須です。")
    private Integer status;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getExpectedRentalOn() {
        return expectedRentalOn;
    }

    public void setExpectedRentalOn(LocalDate expectedRentalOn) {
        this.expectedRentalOn = expectedRentalOn;
    }

    public LocalDate getExpectedReturnOn() {
        return expectedReturnOn;
    }

    public void setExpectedReturnOn(LocalDate expectedReturnOn) {
        this.expectedReturnOn = expectedReturnOn;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
