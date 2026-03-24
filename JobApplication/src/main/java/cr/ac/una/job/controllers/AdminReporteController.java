package cr.ac.una.job.controllers;


import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


import cr.ac.una.job.models.Puesto;
import cr.ac.una.job.models.PuestoCaracteristica;
import cr.ac.una.job.repositories.IPuestoCaracteristicaRepository;
import cr.ac.una.job.repositories.IPuestoRepository;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.awt.Color;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/reportes")
public class AdminReporteController {

    private static final String[] MESES = {
            "Enero","Febrero","Marzo","Abril","Mayo","Junio",
            "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
    };

    private static final String[] NIVEL_LABELS = {
            "", "Basico", "Elemental", "Intermedio", "Avanzado", "Experto"
    };

    private final IPuestoRepository puestoRepository;
    private final IPuestoCaracteristicaRepository puestoCaracteristicaRepository;

    public AdminReporteController(IPuestoRepository puestoRepository,
                                  IPuestoCaracteristicaRepository puestoCaracteristicaRepository) {
        this.puestoRepository = puestoRepository;
        this.puestoCaracteristicaRepository = puestoCaracteristicaRepository;
    }



    @GetMapping("/puestos")
    public String vista(@RequestParam(required = false) Integer anio, Model model) {
        if (anio == null) anio = LocalDateTime.now().getYear();
        final int anioFinal = anio;

        List<Puesto> todos = puestoRepository.findAll();

        Map<Integer, Long> conteoPorMes = todos.stream()
                .filter(p -> p.getCreatedAt() != null
                        && p.getCreatedAt().getYear() == anioFinal)
                .collect(Collectors.groupingBy(
                        p -> p.getCreatedAt().getMonthValue(),
                        Collectors.counting()));

        List<Object[]> datos = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            datos.add(new Object[]{ m, conteoPorMes.getOrDefault(m, 0L) });
        }

        model.addAttribute("anio", anio);
        model.addAttribute("datos", datos);
        model.addAttribute("meses", MESES);
        return "admin/reportes";
    }



    @GetMapping("/puestos/pdf")
    public void generarPdf(@RequestParam(required = false) Integer anio,
                           HttpServletResponse response) throws IOException {

        if (anio == null) anio = LocalDateTime.now().getYear();
        final int anioFinal = anio;

        List<Puesto> todos = puestoRepository.findAll();

        List<Puesto> delAnio = todos.stream()
                .filter(p -> p.getCreatedAt() != null
                        && p.getCreatedAt().getYear() == anioFinal)
                .sorted(Comparator.comparing(Puesto::getCreatedAt))
                .collect(Collectors.toList());

        Map<Integer, Long> conteoPorMes = delAnio.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getCreatedAt().getMonthValue(),
                        Collectors.counting()));


        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"reporte-puestos-" + anio + ".pdf\"");


        Document doc = new Document(PageSize.A4, 50, 50, 60, 50);
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();


        Color colorAzul   = new Color(0x1e, 0x30, 0x48);
        Color colorDorado = new Color(0xe8, 0xa8, 0x38);
        Color colorTexto  = new Color(0xf0, 0xf4, 0xf8);
        Color colorGris   = new Color(0xa8, 0xc0, 0xd6);
        Color colorClaro  = new Color(0xe8, 0xf0, 0xf8);


        Font fTitulo    = FontFactory.getFont(FontFactory.HELVETICA_BOLD,   20, colorDorado);
        Font fSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD,   13, colorAzul);
        Font fNormal    = FontFactory.getFont(FontFactory.HELVETICA,        10, Color.BLACK);
        Font fHeader    = FontFactory.getFont(FontFactory.HELVETICA_BOLD,   10, colorTexto);
        Font fPeque     = FontFactory.getFont(FontFactory.HELVETICA,         9, colorGris);
        Font fLabel     = FontFactory.getFont(FontFactory.HELVETICA_BOLD,    9, colorGris);
        Font fNivel     = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8,
                new Color(0x6a, 0x8c, 0xa8));


        Paragraph pTitulo = new Paragraph("Bolsa de Empleo", fTitulo);
        pTitulo.setAlignment(Element.ALIGN_CENTER);
        pTitulo.setSpacingAfter(4);
        doc.add(pTitulo);

        Paragraph pSubtitulo = new Paragraph(
                "Reporte de Puestos Publicados - Anio " + anio, fSubtitulo);
        pSubtitulo.setAlignment(Element.ALIGN_CENTER);
        pSubtitulo.setSpacingAfter(4);
        doc.add(pSubtitulo);

        String fechaGen = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        Paragraph pFecha = new Paragraph("Generado el: " + fechaGen, fPeque);
        pFecha.setAlignment(Element.ALIGN_CENTER);
        pFecha.setSpacingAfter(16);
        doc.add(pFecha);

        agregarSeparador(doc, colorDorado, 1.5f);


        Paragraph pResumen = new Paragraph("Resumen por Mes", fSubtitulo);
        pResumen.setSpacingBefore(12);
        pResumen.setSpacingAfter(8);
        doc.add(pResumen);

        PdfPTable tablaMes = new PdfPTable(3);
        tablaMes.setWidthPercentage(70);
        tablaMes.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaMes.setSpacingAfter(18);

        for (String h : new String[]{"Mes", "Puestos Publicados", "% del Total"}) {
            PdfPCell cell = new PdfPCell(new Phrase(h, fHeader));
            cell.setBackgroundColor(colorAzul);
            cell.setPadding(7);
            cell.setBorderColor(colorDorado);
            tablaMes.addCell(cell);
        }

        long totalDelAnio = delAnio.size();
        for (int m = 1; m <= 12; m++) {
            long cnt = conteoPorMes.getOrDefault(m, 0L);
            double pct = (totalDelAnio == 0) ? 0.0 : (cnt * 100.0 / totalDelAnio);
            Color bgRow = (m % 2 == 0) ? new Color(0xf9, 0xf9, 0xf9) : Color.WHITE;

            PdfPCell cMes = new PdfPCell(new Phrase(MESES[m - 1], fNormal));
            cMes.setBackgroundColor(bgRow); cMes.setPadding(6);

            PdfPCell cCnt = new PdfPCell(new Phrase(String.valueOf(cnt), fNormal));
            cCnt.setBackgroundColor(bgRow); cCnt.setPadding(6);
            cCnt.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cPct = new PdfPCell(new Phrase(String.format("%.1f%%", pct), fNormal));
            cPct.setBackgroundColor(bgRow); cPct.setPadding(6);
            cPct.setHorizontalAlignment(Element.ALIGN_CENTER);

            tablaMes.addCell(cMes);
            tablaMes.addCell(cCnt);
            tablaMes.addCell(cPct);
        }

        Font fTotalBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, colorDorado);
        PdfPCell cTotalLabel = new PdfPCell(new Phrase("TOTAL", fTotalBold));
        cTotalLabel.setBackgroundColor(colorAzul); cTotalLabel.setPadding(7);

        PdfPCell cTotalNum = new PdfPCell(new Phrase(String.valueOf(totalDelAnio), fTotalBold));
        cTotalNum.setBackgroundColor(colorAzul); cTotalNum.setPadding(7);
        cTotalNum.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell cTotalPct = new PdfPCell(new Phrase("100%", fTotalBold));
        cTotalPct.setBackgroundColor(colorAzul); cTotalPct.setPadding(7);
        cTotalPct.setHorizontalAlignment(Element.ALIGN_CENTER);

        tablaMes.addCell(cTotalLabel);
        tablaMes.addCell(cTotalNum);
        tablaMes.addCell(cTotalPct);
        doc.add(tablaMes);


        if (!delAnio.isEmpty()) {
            agregarSeparador(doc, colorGris, 0.5f);

            Paragraph pDetalle = new Paragraph("Detalle de Puestos Publicados", fSubtitulo);
            pDetalle.setSpacingBefore(8);
            pDetalle.setSpacingAfter(10);
            doc.add(pDetalle);

            Font fNombrePuesto = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, colorTexto);

            int numero = 1;
            for (Puesto p : delAnio) {

                PdfPTable tarjeta = new PdfPTable(1);
                tarjeta.setWidthPercentage(100);
                tarjeta.setSpacingAfter(10);


                PdfPCell cEnc = new PdfPCell();
                cEnc.setBackgroundColor(colorAzul);
                cEnc.setPadding(8);
                cEnc.setBorderColor(colorDorado);
                cEnc.setBorderWidth(1.2f);
                Paragraph pNombre = new Paragraph(numero + ". " + p.getTitulo(), fNombrePuesto);
                pNombre.setSpacingAfter(0);
                cEnc.addElement(pNombre);
                tarjeta.addCell(cEnc);


                PdfPCell cCuerpo = new PdfPCell();
                cCuerpo.setPadding(8);
                cCuerpo.setBorderColor(new Color(0xcc, 0xcc, 0xcc));

                String empresa = (p.getEmpresa() != null) ? p.getEmpresa().getNombre() : "-";
                String salario = "CRC " + String.format("%,.0f", p.getSalario());
                String estado  = p.getEstado().name();
                String fecha   = (p.getCreatedAt() != null)
                        ? p.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "-";

                PdfPTable infoRow = new PdfPTable(new float[]{2.5f, 2f, 1.5f, 1.5f});
                infoRow.setWidthPercentage(100);

                for (String[] par : new String[][]{
                        {"Empresa",   empresa},
                        {"Salario",   salario},
                        {"Estado",    estado},
                        {"Publicado", fecha}
                }) {
                    PdfPCell ci = new PdfPCell();
                    ci.setBorder(Rectangle.NO_BORDER);
                    ci.setPaddingBottom(4);
                    Paragraph pl = new Paragraph(par[0], fLabel);
                    pl.setSpacingAfter(1);
                    Paragraph pv = new Paragraph(par[1], fNormal);
                    ci.addElement(pl);
                    ci.addElement(pv);
                    infoRow.addCell(ci);
                }
                cCuerpo.addElement(infoRow);


                List<PuestoCaracteristica> caracteristicas =
                        puestoCaracteristicaRepository.findByPuestoIdPuesto(p.getIdPuesto());

                if (!caracteristicas.isEmpty()) {
                    Paragraph pCaractTit = new Paragraph("Caracteristicas requeridas:", fLabel);
                    pCaractTit.setSpacingBefore(6);
                    pCaractTit.setSpacingAfter(3);
                    cCuerpo.addElement(pCaractTit);

                    PdfPTable tCaract = new PdfPTable(new float[]{3f, 2f, 2f});
                    tCaract.setWidthPercentage(100);

                    for (String hc : new String[]{"Caracteristica", "Categoria", "Nivel requerido"}) {
                        PdfPCell ch = new PdfPCell(new Phrase(hc, fLabel));
                        ch.setBackgroundColor(colorClaro);
                        ch.setPadding(4);
                        ch.setBorderColor(new Color(0xcc, 0xcc, 0xcc));
                        tCaract.addCell(ch);
                    }

                    for (PuestoCaracteristica pc : caracteristicas) {
                        String nombreCar = pc.getCaracteristica().getNombre();
                        String categoria = (pc.getCaracteristica().getPadre() != null)
                                ? pc.getCaracteristica().getPadre().getNombre() : "-";
                        int nivelNum = pc.getNivelRequerido();
                        String nivelTexto = (nivelNum >= 1 && nivelNum <= 5)
                                ? nivelNum + " - " + NIVEL_LABELS[nivelNum]
                                : String.valueOf(nivelNum);

                        PdfPCell cNom = new PdfPCell(new Phrase(nombreCar, fNormal));
                        cNom.setPadding(4);

                        PdfPCell cCat = new PdfPCell(new Phrase(categoria, fNivel));
                        cCat.setPadding(4);

                        PdfPCell cNiv = new PdfPCell(new Phrase(nivelTexto, fNormal));
                        cNiv.setPadding(4);
                        cNiv.setHorizontalAlignment(Element.ALIGN_CENTER);

                        tCaract.addCell(cNom);
                        tCaract.addCell(cCat);
                        tCaract.addCell(cNiv);
                    }
                    cCuerpo.addElement(tCaract);

                } else {
                    Paragraph sinCar = new Paragraph("Sin caracteristicas requeridas.", fNivel);
                    sinCar.setSpacingBefore(5);
                    cCuerpo.addElement(sinCar);
                }

                tarjeta.addCell(cCuerpo);
                doc.add(tarjeta);
                numero++;
            }
        }


        agregarSeparador(doc, colorGris, 0.5f);
        Paragraph pie = new Paragraph(
                "Bolsa de Empleo - UNA - Programacion IV - " + anio, fPeque);
        pie.setAlignment(Element.ALIGN_CENTER);
        doc.add(pie);

        doc.close();
    }


    private void agregarSeparador(Document doc, Color color, float grosor) throws IOException {
        PdfPTable sep = new PdfPTable(1);
        sep.setWidthPercentage(100);
        sep.setSpacingBefore(6);
        sep.setSpacingAfter(6);

        PdfPCell cell = new PdfPCell(new Phrase(" "));
        cell.setBorder(Rectangle.BOTTOM);
        cell.setBorderColorBottom(color);
        cell.setBorderWidthBottom(grosor);
        cell.setPadding(0);
        cell.setFixedHeight(grosor + 2);
        sep.addCell(cell);

        doc.add(sep);
    }
}