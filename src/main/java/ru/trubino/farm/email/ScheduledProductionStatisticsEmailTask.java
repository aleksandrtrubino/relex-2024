package ru.trubino.farm.email;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.trubino.farm.production.ProductionEntry;
import ru.trubino.farm.production.ProductionEntryService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Component
public class ScheduledProductionStatisticsEmailTask {

    @Autowired
    ProductionEntryService productionEntryService;

    @Autowired
    EmailUtil emailUtil;

    @Value("${app.owner.mail}")
    String ownerMail;

    @Scheduled(cron = "00 24 17 * * ?")
    public void sendScheduledProductionStatisticsEmail() {

        String to = ownerMail;
        String subject = "Статистика производства";

        LocalDateTime fromTime = LocalDateTime.of(LocalDate.now(),LocalTime.of(0,0,0));
        LocalDateTime toTime = LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59,59));

        List<ProductionEntry> productionEntries = productionEntryService.getProductionEntriesStatisticsBy(null,toTime,null,null);
        productionEntries.sort(Comparator.comparing(entry -> entry.getProduct().getName()));

        String body = generateStatisticsHtml(productionEntries);

        try{
            emailUtil.sendHtmlMessage(to, subject, body);

        }catch (MessagingException e){
            throw new RuntimeException("");
        }
    }

    private String generateStatisticsHtml(List<ProductionEntry> productionEntries){
        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Статистика производства фермы (").append(LocalDate.now().toString()).append(")</h2>");

        if(productionEntries != null && !productionEntries.isEmpty()){
            html.append("<table border='1'><tr>");
            html.append("<th>Наименование</th>");
            html.append("<th>Работник</th>");
            html.append("<th>Количество</th>");
            html.append("<th>Единица измерения</th>");
            html.append("</tr>");

            for(ProductionEntry entry : productionEntries){
                html.append("<tr>");
                html.append("<td>").append(entry.getProduct().getName()).append("</td>");
                html.append("<td>")
                        .append(entry.getProducer().getLastName())
                        .append(" ")
                        .append(entry.getProducer().getFirstName())
                        .append(" ")
                        .append(entry.getProducer().getMiddleName())
                        .append("</td>");
                html.append("<td>").append(entry.getQuantity()).append("</td>");
                html.append("<td>").append(entry.getProduct().getUnit().getName()).append("</td>");
                html.append("</tr>");
            }
        } else {
            html.append("<p>Нет записей в производственном журнале</p>");
        }

        html.append("</body></html>");

        return html.toString();
    }

}
