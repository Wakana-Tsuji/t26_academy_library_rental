package jp.co.metateam.library.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import jp.co.metateam.library.model.Account;
import jp.co.metateam.library.model.RentalManageDto;
import jp.co.metateam.library.model.Stock;
import jp.co.metateam.library.service.AccountService;
import jp.co.metateam.library.service.StockService;
import jp.co.metateam.library.service.RentalService;
import jp.co.metateam.library.values.RentalStatus;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;

//貸出登録クラス

@Log4j2
@Controller
@RequestMapping("/rental")
public class RentalManageController {

    // 貸出一覧画面初期表示

    // @param model
    // @return

    @GetMapping("/index")
    public String index(Model model) {
        // 貸出管理テーブルから全件取得
        // 貸出一覧画面に渡すデータをmodelに追加
        model.addAttribute("list", rentalService.findAll());
        // 貸出一覧画面に遷移
        return "/rental/index";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        log.info("貸出登録画面表示");

        model.addAttribute("title", "貸出登録");

        List<Account> accountList = accountService.findAll();

        model.addAttribute("accounts", accountList);

        List<Stock> stockList = stockService.findAll();

        model.addAttribute("stockList", stockList);

        model.addAttribute("rentalStatus", RentalStatus.values());

        model.addAttribute("rentalManageDto", new RentalManageDto());

        return "/rental/add";
    }

    @Autowired
    private AccountService accountService;

    @Autowired
    private StockService stockService;

    @Autowired
    private RentalService rentalService;

    @PostMapping("/add")
    public String addRental(
            @Valid @ModelAttribute RentalManageDto rentalManageDto,
            BindingResult bindingResult,
            Model model) {

        // 貸出予定日
        if (bindingResult.hasFieldErrors("expectedRentalOn")
                && rentalManageDto.getExpectedRentalOn() == null) {

            bindingResult.rejectValue(
                    "expectedRentalOn",
                    "",
                    "yyyy-MM-ddの形式で入力してください。");
        }

        // 返却予定日
        if (bindingResult.hasFieldErrors("expectedReturnOn")
                && rentalManageDto.getExpectedReturnOn() == null) {

            bindingResult.rejectValue(
                    "expectedReturnOn",
                    "",
                    "yyyy-MM-ddの形式で入力してください。");
        }

        // 必須チェック
        boolean hasRentalDate = rentalManageDto.getExpectedRentalOn() != null;
        boolean hasReturnDate = rentalManageDto.getExpectedReturnOn() != null;
        boolean hasStatus = rentalManageDto.getStatus() != null;

        boolean rentalDateFieldValid = !bindingResult.hasFieldErrors("expectedRentalOn");

        boolean returnDateFieldValid = !bindingResult.hasFieldErrors("expectedReturnOn");

        boolean statusFieldValid = !bindingResult.hasFieldErrors("status");

        // 日付妥当性チェック
        if (hasRentalDate && hasReturnDate
                && rentalDateFieldValid && returnDateFieldValid) {

            if (rentalManageDto.getExpectedReturnOn().isBefore(rentalManageDto.getExpectedRentalOn())) {
                bindingResult.rejectValue(
                        "expectedReturnOn",
                        "",
                        "返却予定日は貸出予定日以降にしてください。");
            }
        }

        // ステータスチェック
        if (hasStatus && statusFieldValid) {

            int status = rentalManageDto.getStatus();

            // ステータスが返却済みかキャンセル出ないか
            if (status == RentalStatus.RETURNED.getValue()
                    || status == RentalStatus.CANCELED.getValue()) {

                bindingResult.rejectValue(
                        "status",
                        "",
                        "貸出ステータスが『返却済み』または『キャンセル』になっています。");
            }

            // ステータス別日付チェック
            if (hasRentalDate) {

                LocalDate today = LocalDate.now();

                // 貸出中
                if (status == RentalStatus.RENTAlING.getValue()) {

                    if (rentalManageDto.getExpectedRentalOn().isAfter(today)) {
                        bindingResult.rejectValue(
                                "expectedRentalOn",
                                "",
                                "貸出中の場合、貸出予定日は現在日付か過去日付を入力してください。");
                    }
                }

                // 貸出待ち
                if (status == RentalStatus.RENT_WAIT.getValue()) {

                    if (!rentalManageDto.getExpectedRentalOn().isAfter(today)) {
                        bindingResult.rejectValue(
                                "expectedRentalOn",
                                "",
                                "貸出待ちの場合、貸出予定日は未来日付を入力してください。");
                    }
                }
            }
        }

        // 貸出可否チェック（Service）
        if (!bindingResult.hasErrors()) {

            String error = rentalService.validateBusiness(rentalManageDto);

            if (error != null) {
                bindingResult.rejectValue(
                        "stockId",
                        "",
                        error);
            }
        }

        // エラー判定
        if (bindingResult.hasErrors()) {
            setCommonModel(model);
            return "rental/add";
        }

        // 保存処理
        rentalService.save(rentalManageDto);
        return "redirect:/rental/index";
    }

    private void setCommonModel(Model model) {
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("stockList", stockService.findAll());
        model.addAttribute("rentalStatus", RentalStatus.values());
    }
}
