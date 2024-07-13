package com.firmino.hinedigital.extensions

import com.firmino.hinedigital.R

enum class ParagraphType { PARAGRAPH, TOPIC }
data class Title(val title: String, val items: List<Paragraph>)
data class Paragraph(val id: Int, val type: ParagraphType)

val guideItems = listOf(
    Title(
        title = "Introdução",
        items = listOf(
            Paragraph(R.string.guide_p1, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p2, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p3, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Registrando o exame",
        items = listOf(
            Paragraph(R.string.guide_p4, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p5, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p6, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p7, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p8, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p9, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p10, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p11, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Pontuação dos achados neurológicos",
        items = listOf(
            Paragraph(R.string.guide_p12, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p13, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p14, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p15, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p16, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p17, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p18, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p19, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p20, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Pontuação com itens ausentes",
        items = listOf(
            Paragraph(R.string.guide_p21, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p22, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Seção 1 – Função dos Nervos Cranianos",
        items = listOf(
            Paragraph(R.string.guide_p23, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p24, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p25, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p26, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p27, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p28, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Seção 2-5",
        items = listOf(
            Paragraph(R.string.guide_p29, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Seção 2 – Itens de Postura",
        items = listOf(
            Paragraph(R.string.guide_p30, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p31, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p32, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p33, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p34, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p35, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Seção 3 – Movimentos",
        items = listOf(
            Paragraph(R.string.guide_p36, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Seção 4 – Itens de Tônus",
        items = listOf(
            Paragraph(R.string.guide_p37, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p38, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p39, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p40, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p41, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p42, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p43, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p44, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p45, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Seção 5 – Reflexos e Reações",
        items = listOf(
            Paragraph(R.string.guide_p46, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p47, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p48, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p49, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p50, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Marcos Motores e Comportamento",
        items = listOf(
            Paragraph(R.string.guide_p51, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p52, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p53, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p54, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p55, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p56, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p57, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Referências",
        items = listOf(
            Paragraph(R.string.guide_p58, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p59, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p60, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p61, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p62, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p63, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p64, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p65, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p66, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p67, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p68, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p69, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p70, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p71, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p72, ParagraphType.TOPIC),
            Paragraph(R.string.guide_p73, ParagraphType.PARAGRAPH),
        )
    ),
)