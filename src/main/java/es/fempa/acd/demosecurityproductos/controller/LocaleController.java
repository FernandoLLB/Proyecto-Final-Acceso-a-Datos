package es.fempa.acd.demosecurityproductos.controller;

import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/locale")
public class LocaleController {

    private final LocaleResolver localeResolver;

    public LocaleController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping("/change")
    public String changeLocale(@RequestParam("lang") String lang,
                               @RequestParam(value = "redirect", defaultValue = "/login") String redirectUrl,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        Locale locale = Locale.forLanguageTag(lang);
        localeResolver.setLocale(request, response, locale);

        return "redirect:" + redirectUrl;
    }
}
