package com.MindHub.homebanking.services.implement;

import com.MindHub.homebanking.models.Account;
import com.MindHub.homebanking.models.Transaction;
import com.MindHub.homebanking.services.AccountService;
import com.MindHub.homebanking.services.PDFGeneratorService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PDFGeneratorServiceImpl implements PDFGeneratorService {


    @Autowired
    private AccountService accountService;


    @Override
    public void export(HttpServletResponse response, long id, LocalDate desde, LocalDate hasta) throws IOException, DocumentException {
        Account account = accountService.findByID(id);

        Set<Transaction> transaction = account.getTransactions();
        Set<Transaction> transactionSet = transaction.stream().filter(transaction1 -> transaction1.getDate().isBefore(hasta.plusDays(1))).collect(Collectors.toSet());
        transactionSet.stream().filter(transaction1 -> transaction1.getDate().isAfter(desde));

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());


        document.open();
        Phrase p = new Phrase("Cuenta: "+account.getNumber());
        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Fecha");
        table.addCell("Descripcion");
        table.addCell("Tipo");
        table.addCell("Monto");
        table.addCell("Saldo Actual");

        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j=0;j<cells.length;j++){
            cells[j].setBackgroundColor(new Color(98, 56, 232));
        }

        transactionSet.stream().forEach(transaction1 -> {
            PdfPCell c1 = new PdfPCell(new Phrase(transaction1.getDate() + ""));
            PdfPCell c2 = new PdfPCell(new Phrase(transaction1.getDescription()));
            PdfPCell c3 = new PdfPCell(new Phrase(transaction1.getType() + ""));
            PdfPCell c4 = new PdfPCell(new Phrase(transaction1.getAmount() + ""));
            PdfPCell c5 = new PdfPCell(new Phrase(transaction1.getBalance() + ""));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c4.setHorizontalAlignment(Element.ALIGN_CENTER);
            c5.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);
        });
        document.add(table);
        document.close();
    }
}
