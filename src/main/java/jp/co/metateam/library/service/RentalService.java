package jp.co.metateam.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.metateam.library.model.Rental;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.model.Stock;
import jp.co.metateam.library.repository.RentalRepository;
import jp.co.metateam.library.repository.StockRepository;
import jp.co.metateam.library.values.RentalStatus;
import jp.co.metateam.library.values.StockStatus;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    // 貸出登録
    @Transactional
    public void save(RentalManageDto dto) {

        Rental rental = new Rental();

        // DTO→Entityに変換
        rental.setEmployeeId(dto.getEmployeeId());
        rental.setExpectedRentalOn(dto.getExpectedRentalOn());
        rental.setExpectedReturnOn(dto.getExpectedReturnOn());
        rental.setStockId(dto.getStockId());
        rental.setStatus(dto.getStatus());

        // DB保存
        rentalRepository.save(rental);
    }

    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

    @Autowired
    private StockRepository stockRepository;

    public String validateBusiness(RentalManageDto dto) {

        // 在庫取得

        Stock stock = stockRepository.findById(dto.getStockId()).orElse(null);

        if (stock == null) {
            return "在庫が存在しません。";
        }

        // 在庫ステータスチェック

        if (stock.getStatus() != StockStatus.RENT_AVAILABLE.getValue()) {
            return "この在庫は貸出できません。";
        }

        // 予約データ取得
        List<Rental> list = rentalRepository.findByStockIdAndStatusIn(
                dto.getStockId(),
                List.of(
                        RentalStatus.RENTAlING.getValue(),
                        RentalStatus.RENT_WAIT.getValue()));

        // 重複チェック
        for (Rental r : list) {

            boolean noOverlap = r.getExpectedReturnOn().isBefore(dto.getExpectedRentalOn())
                    || dto.getExpectedReturnOn().isBefore(r.getExpectedRentalOn());

            if (!noOverlap) {
                return "この期間は既に貸し出されているか、貸出予約があるため貸出できません。";
            }
        }

        return null;
    }

}