package com.firmino.hinedigital.extensions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.firmino.hinedigital.R

enum class ParagraphType { PARAGRAPH, TOPIC, IMAGE, SUBTITLE }
data class Title(val title: String, val icon: Int? = null, val smallTitle: String, val items: List<Paragraph>)
data class Paragraph(val id: Int, val type: ParagraphType, val priprity: Int = 0)

@Composable
fun ContentText(modifier: Modifier = Modifier, content: Title) {
    Column(modifier.padding(12.dp)) {
        Text(text = content.title, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(8.dp))
        content.items.forEach {
            when (it.type) {
                ParagraphType.PARAGRAPH -> TextParagraph(text = stringResource(id = it.id))
                ParagraphType.TOPIC -> TextTopic(text = stringResource(id = it.id), priority = it.priprity)
                ParagraphType.IMAGE -> TextImage(it.id)
                ParagraphType.SUBTITLE -> TextSubtitle(text = stringResource(id = it.id))
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TextTopic(text: String, priority: Int = 0) {
    val prefix = when (priority) {
        1 -> "○"
        2 -> "➤"
        else -> "●"
    }
    Row {
        Spacer(modifier = Modifier.width(((priority + 1) * 8).dp))
        Text(text = prefix, textAlign = TextAlign.Justify, style = MaterialTheme.typography.bodyMedium, color = Color.White)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text, textAlign = TextAlign.Justify, style = MaterialTheme.typography.bodyMedium, color = Color.White)
    }
}

@Composable
private fun TextParagraph(text: String) {
    Text(text = text, textAlign = TextAlign.Justify, style = MaterialTheme.typography.bodyMedium, color = Color.White)
}

@Composable
private fun TextSubtitle(text: String) {
    Text(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), text = text, color = Color.White, style = MaterialTheme.typography.titleMedium)
}

@Composable
private fun TextImage(image: Int) {
    ElevatedCard(
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Image(modifier = Modifier.padding(12.dp).fillMaxWidth(), painter = painterResource(id = image), contentDescription = null, contentScale = ContentScale.FillWidth)
    }
}

val informationItems = listOf(
    Title(
        title = "Folder I",
        smallTitle = "Folder I",
        icon = R.drawable.ic_folder,
        items = listOf(
            Paragraph(R.drawable.bg_hine_folder, ParagraphType.IMAGE)
        )
    ),
    Title(
        title = "Informações Detalhadas",
        smallTitle = "Detalhes I",
        icon = R.drawable.ic_introduction,
        items = listOf(
            Paragraph(R.string.info_p1, ParagraphType.PARAGRAPH),
            Paragraph(R.string.info_p2, ParagraphType.SUBTITLE),
            Paragraph(R.string.info_p3, ParagraphType.PARAGRAPH),
            Paragraph(R.string.info_p4, ParagraphType.SUBTITLE),
            Paragraph(R.string.info_p5, ParagraphType.PARAGRAPH),
            Paragraph(R.string.info_p6, ParagraphType.TOPIC),
            Paragraph(R.string.info_p7, ParagraphType.TOPIC),
            Paragraph(R.string.info_p8, ParagraphType.TOPIC),
            Paragraph(R.string.info_p9, ParagraphType.TOPIC),
            Paragraph(R.string.info_p10, ParagraphType.TOPIC),
            Paragraph(R.string.info_p11, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Informações Detalhadas",
        smallTitle = "Detalhes II",
        icon = R.drawable.ic_introduction,
        items = listOf(
            Paragraph(R.string.info_p12, ParagraphType.SUBTITLE),
            Paragraph(R.string.info_p13, ParagraphType.TOPIC),
            Paragraph(R.string.info_p14, ParagraphType.TOPIC),
            Paragraph(R.string.info_p15, ParagraphType.TOPIC),
            Paragraph(R.string.info_p16, ParagraphType.TOPIC),
            Paragraph(R.string.info_p17, ParagraphType.SUBTITLE),
            Paragraph(R.string.info_p18, ParagraphType.TOPIC),
            Paragraph(R.string.info_p19, ParagraphType.TOPIC),
            Paragraph(R.string.info_p20, ParagraphType.TOPIC),
            Paragraph(R.string.info_p21, ParagraphType.TOPIC),
            Paragraph(R.string.info_p22, ParagraphType.TOPIC),
            Paragraph(R.string.info_p23, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Diagnóstico",
        smallTitle = "Diagnóstico",
        icon = R.drawable.ic_diagnosis,
        items = listOf(
            Paragraph(R.string.info_p24, ParagraphType.SUBTITLE),
            Paragraph(R.string.info_p25, ParagraphType.TOPIC),
            Paragraph(R.string.info_p26, ParagraphType.SUBTITLE),
            Paragraph(R.string.info_p27, ParagraphType.TOPIC),
            Paragraph(R.string.info_p28, ParagraphType.TOPIC),
            Paragraph(R.string.info_p29, ParagraphType.SUBTITLE),
            Paragraph(R.string.info_p30, ParagraphType.TOPIC),
            Paragraph(R.string.info_p31, ParagraphType.TOPIC),
            Paragraph(R.string.info_p32, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Neuroproteção",
        smallTitle = "Neuroproteção",
        icon = R.drawable.ic_neuroprotection,
        items = listOf(
            Paragraph(R.string.info_p33, ParagraphType.PARAGRAPH),
            Paragraph(R.string.info_p34, ParagraphType.TOPIC),
            Paragraph(R.string.info_p35, ParagraphType.TOPIC, 1),
            Paragraph(R.string.info_p36, ParagraphType.TOPIC, 1),
            Paragraph(R.string.info_p37, ParagraphType.TOPIC),
            Paragraph(R.string.info_p38, ParagraphType.TOPIC, 1),
            Paragraph(R.string.info_p39, ParagraphType.TOPIC, 1),
            Paragraph(R.string.info_p40, ParagraphType.TOPIC, 1),
            Paragraph(R.string.info_p41, ParagraphType.TOPIC, 1),
            Paragraph(R.string.info_p42, ParagraphType.TOPIC, 1),
            Paragraph(R.string.info_p43, ParagraphType.TOPIC),
            Paragraph(R.string.info_p44, ParagraphType.TOPIC),
            Paragraph(R.string.info_p45, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Reflexos Primitivos I",
        smallTitle = "Reflexos I",
        icon = R.drawable.ic_icon1,
        items = listOf(
            Paragraph(R.string.info_p46, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Reflexos Primitivos II",
        smallTitle = "Reflexos II",
        icon = R.drawable.ic_icon2,
        items = listOf(
            Paragraph(R.drawable.bg_reflexes1, ParagraphType.IMAGE),
        )
    ),
    Title(
        title = "Reflexos Primitivos III",
        smallTitle = "Reflexos III",
        icon = R.drawable.ic_icon3,
        items = listOf(
            Paragraph(R.drawable.bg_reflexes2, ParagraphType.IMAGE),
        )
    ),
    Title(
        title = "Neuroplasticidade cerebral 1000 dias de vida",
        smallTitle = "Neuroplasticidade",
        icon = R.drawable.ic_neuroplastic,
        items = listOf(
            Paragraph(R.string.info_p47, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Marcos Do Desenvolvimento Neuropsicomotor",
        smallTitle = "Neuropsicomotor",
        icon = R.drawable.ic_neuropsychomotor,
        items = listOf(
            Paragraph(R.string.info_p48, ParagraphType.PARAGRAPH),
            Paragraph(R.string.info_p49, ParagraphType.SUBTITLE),
            Paragraph(R.string.info_p50, ParagraphType.TOPIC),
            Paragraph(R.string.info_p51, ParagraphType.TOPIC),
            Paragraph(R.string.info_p52, ParagraphType.TOPIC),
            Paragraph(R.string.info_p53, ParagraphType.TOPIC),
        )
    ),
    Title(
        title = "Folder II",
        smallTitle = "Folder II",
        icon = R.drawable.ic_folder,
        items = listOf(
            Paragraph(R.drawable.bg_hine_folder2, ParagraphType.IMAGE)
        )
    ),
)

val guideItems = listOf(
    Title(
        title = "Introdução",
        smallTitle = "Introdução",
        icon = R.drawable.ic_introduction,
        items = listOf(
            Paragraph(R.string.guide_p1, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p2, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p3, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Registrando o exame",
        smallTitle = "Registrando",
        icon = R.drawable.ic_registry,
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
        smallTitle = "Achados Neurológicos",
        icon = R.drawable.ic_findfile,
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
        smallTitle = "Itens ausentes",
        icon = R.drawable.ic_emptyitem,
        items = listOf(
            Paragraph(R.string.guide_p21, ParagraphType.PARAGRAPH),
            Paragraph(R.string.guide_p22, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Seção 1 – Função dos Nervos Cranianos",
        smallTitle = "Seção 1",
        icon = R.drawable.ic_icon1,
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
        smallTitle = "Seção 2-5",
        icon = R.drawable.ic_icon25,
        items = listOf(
            Paragraph(R.string.guide_p29, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Seção 2 – Itens de Postura",
        smallTitle = "Seção 2",
        icon = R.drawable.ic_icon2,
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
        smallTitle = "Seção 3",
        icon = R.drawable.ic_icon3,
        items = listOf(
            Paragraph(R.string.guide_p36, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "Seção 4 – Itens de Tônus",
        smallTitle = "Seção 4",
        icon = R.drawable.ic_icon4,
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
        smallTitle = "Seção 5",
        icon = R.drawable.ic_icon5,
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
        smallTitle = "Marcos & Comportamento",
        icon = R.drawable.ic_baby,
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
        smallTitle = "Referências",
        icon = R.drawable.ic_references,
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

val policyItems = listOf(
    Title(
        title = "1. Tipos de dados coletados",
        smallTitle = "Tipos de Dados",
        icon = R.drawable.ic_policy1,
        items = listOf(
            Paragraph(R.string.policy_p1, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p2, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p3, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "2. Modo e local de processamento dos dados",
        smallTitle = "Processamento",
        icon = R.drawable.ic_policy2,
        items = listOf(
            Paragraph(R.string.policy_p4, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p5, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p6, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p7, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p8, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p9, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p10, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p11, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "3. Informações detalhadas sobre o processamento de dados pessoais",
        smallTitle = "Detalhes",
        icon = R.drawable.ic_policy3,
        items = listOf(
            Paragraph(R.string.policy_p12, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p13, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p14, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p15, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "4. Informações adicionais sobre a coleta e processamento de dados",
        smallTitle = "Coleta de Dados",
        icon = R.drawable.ic_policy4,
        items = listOf(
            Paragraph(R.string.policy_p16, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p17, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "5. Informações adicionais sobre os dados pessoais do usuário",
        smallTitle = "Dados Pessoais",
        icon = R.drawable.ic_policy5,
        items = listOf(
            Paragraph(R.string.policy_p18, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "6. Logs do sistema e manutenção",
        smallTitle = "Logs",
        icon = R.drawable.ic_policy6,
        items = listOf(
            Paragraph(R.string.policy_p19, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "7. As informações não contidas nesta política",
        smallTitle = "Outro",
        icon = R.drawable.ic_policy7,
        items = listOf(
            Paragraph(R.string.policy_p20, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "8. Os direitos dos usuários",
        smallTitle = "Direitos",
        icon = R.drawable.ic_policy8,
        items = listOf(
            Paragraph(R.string.policy_p21, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p22, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p23, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "9. Mudanças nesta política de privacidade",
        smallTitle = "Mudanças",
        icon = R.drawable.ic_policy9,
        items = listOf(
            Paragraph(R.string.policy_p24, ParagraphType.PARAGRAPH),
        )
    ),
    Title(
        title = "10. Definições e referências jurídicas",
        smallTitle = "Definições",
        icon = R.drawable.ic_policy10,
        items = listOf(
            Paragraph(R.string.policy_p25, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p26, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p27, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p28, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p29, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p30, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p31, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p32, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p33, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p34, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p35, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p36, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p37, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p38, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p39, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p40, ParagraphType.PARAGRAPH),
            Paragraph(R.string.policy_p41, ParagraphType.TOPIC),
            Paragraph(R.string.policy_p42, ParagraphType.SUBTITLE),
            Paragraph(R.string.policy_p43, ParagraphType.TOPIC),
        )
    ),
)