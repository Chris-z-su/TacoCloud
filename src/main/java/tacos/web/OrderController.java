package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Order;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/current")
    public String orderForm(Model model) {
        model.addAttribute("order", new Order());
        return "OrderForm";
    }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors) {
        //表单校验，如果提交的表单不符合规则，将重新展现表单,并显示错误信息
        if (errors.hasErrors()){
            return "OrderForm";
        }

        log.info("Order submitted: " + order);
        return "redirect:/";
    }

}
