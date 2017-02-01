/**
 * Czech localization.
 */

const Constants = require('../constants/Constants');

module.exports = {
    'locale': 'cs',

    'messages': {
        'add': 'Přidat',
        'back': 'Zpět',
        'cancel': 'Zrušit',
        'open': 'Otevřít',
        'close': 'Zavřít',
        'cancel-tooltip': 'Zrušit a zahodit změny',
        'save': 'Uložit',
        'delete': 'Smazat',
        'remove': 'Odstranit',
        'headline': 'Název',
        'name': 'Název',
        'summary': 'Shrnutí',
        'narrative': 'Popis',
        'fileNo': 'Číslo záznamu',
        'table-actions': 'Akce',
        'table-edit': 'Editovat',
        'save-success-message': 'Hlášení úspěšně uloženo.',
        'save-failed-message': 'Hlášení nelze uložit. Odpověď serveru: ',
        'author': 'Autor',
        'author-title': 'Autor hlášení',
        'description': 'Popis',
        'select.default': '--- Vybrat ---',
        'yes': 'Ano',
        'no': 'Ne',
        'unknown': 'Neznámé',
        'uploading-mask': 'Nahrávám',
        'please-wait': 'Prosím čekejte...',

        'detail.save-tooltip': 'Uložit změny',
        'detail.saving': 'Ukládám...',
        'detail.invalid-tooltip': 'Některá povinná pole nejsou vyplněna',
        'detail.large-time-diff-tooltip': 'Časový rozdíl počátku a konce události je příliš velký',
        'detail.submit': 'Nová revize',
        'detail.submit-tooltip': 'Vytvořit novou revizi tohoto hlášení',
        'detail.submit-success-message': 'Zpráva úspěšně odeslána.',
        'detail.submit-failed-message': 'Hlášení se nepodařilo odeslat. Odpověď serveru: ',
        'detail.phase-transition-success-message': 'Hlášení úspěšně převedeno do další fáze.',
        'detail.phase-transition-failed-message': 'Přechod do další fáze se nezdařil. Zachycena chyba: ',
        'detail.loading': 'Načítám hlášení...',
        'detail.not-found.title': 'Hlášení nenalezeno',
        'detail.remove-failed-message': 'Hlášení se nepodařilo odstranit. Odpověď serveru: ',


        'login.title': Constants.APP_NAME + ' - Přihlášení',
        'login.username': 'Uživatelské jméno',
        'login.password': 'Heslo',
        'login.submit': 'Přihlásit',
        'login.register': 'Registrace',
        'login.error': 'Přihlášení se nezdařilo.',
        'login.progress-mask': 'Přihlašuji...',

        'register.title': Constants.APP_NAME + ' - Nový uživatel',
        'register.first-name': 'Jméno',
        'register.last-name': 'Příjmení',
        'register.username': 'Uživatelské jméno',
        'register.password': 'Heslo',
        'register.password-confirm': 'Potvrzení hesla',
        'register.passwords-not-matching-tooltip': 'Heslo a jeho potvrzení se neshodují',
        'register.submit': 'Registrovat',
        'register.mask': 'Registruji...',

        'main.dashboard-nav': 'Hlavní strana',
        'main.reports-nav': 'Hlášení',
        'main.statistics-nav': 'Statistiky',
        'main.logout': 'Odhlásit se',
        'main.search-placeholder': 'Hledat',
        'main.search.fulltext': 'Hledat ve všech popisech',
        'main.search.fulltext.label': 'Hledat všude',
        'main.search.fulltext-tooltip': 'Hledat daný výraz ve všech hlášeních',
        'main.tacr-notice': 'Projekt byl podpořen Technologickou agenturou České republiky.',

        'dashboard.welcome': 'Dobrý den, {name}, vítejte v ' + Constants.APP_NAME + '.',
        'dashboard.create-new-occurrence-report-tile': 'Nové hlášení o události',
        'dashboard.create-new-safety-issue-tile': 'Nová safety issue',
        'dashboard.create-new-audit-report-tile': 'Nová zpráva o auditu',
        'dashboard.create-import-tile': 'Importovat hlášení',
        'dashboard.recent-panel-heading': 'Nedávno přidaná/upravená hlášení',
        'dashboard.recent-table-last-edited': 'Naposledy upraveno',
        'dashboard.recent.no-reports': 'Zatím nebylo vytvořeno žádné hlášení.',
        'dashboard.import.import-e5': 'Importovat hlášení ve formátu E5X/E5F',
        'dashboard.import.import-safa': 'Importovat SAFA Excel',

        'dashboard.unprocessed': 'Máte {count} nezpracovaných hlášení.',

        'dropzone.title': 'Přetáhněte sem soubor nebo klikněte pro výběr souboru k nahrání.',
        'dropzone-tooltip': 'Klikněte zde pro výber souboru k nahrání',

        'reports.no-reports': 'Nenalezena žádná hlášení. Nové hlášení můžete vytvořit ',
        'reports.no-reports.link': 'zde.',
        'reports.no-reports.link-tooltip': 'Jít na hlavní stránku',
        'reports.open-tooltip': 'Kliknutím zobrazíte detail hlášení a můžete hlášení upravovat',
        'reports.delete-tooltip': 'Smazat toto hlášení',
        'reports.loading-mask': 'Nahrávám hlášení...',
        'reports.panel-title': 'Hlášení',
        'reports.table-date': 'Datum a čas',
        'reports.table-date.tooltip': 'Datum a čas hlášené události',
        'reports.table-moreinfo': 'Další informace',
        'reports.table-type': 'Typ hlášení',
        'reports.table-classification': 'Kategorie',
        'reports.table-classification.tooltip': 'Vyberte kategorií událostí, kterou chcete zobrazit',
        'reports.phase': 'Stav hlášení',
        'reports.filter.label': 'Zobrazit',
        'reports.filter.type.tooltip': 'Vyberte typ hlášení, která chcete zobrazit',
        'reports.filter.type.all': 'Všechna',
        'reports.filter.type.label': 'Filtr typu hlášení:',
        'reports.filter.no-matching-found': 'Žádná hlášení neodpovídají zvoleným parametrům.',
        'reports.filter.reset': 'Zrušit filtry',
        'reports.paging.item-count': 'Zobrazuji {showing} z {total} položek.',
        'reports.create-report': 'Nové hlášení',
        'reports.type.filter': 'Typ hlášení:',

        'filters.label': 'Filtry',

        'delete-dialog.title': 'Smazat hlášení?',
        'delete-dialog.content': 'Skutečně chcete smazat toto hlášení?',
        'delete-dialog.submit': 'Smazat',

        'occurrence.headline-tooltip': 'Krátké pojmenování události - pole je povinné',
        'occurrence.start-time': 'Počátek události',
        'occurrence.start-time-tooltip': 'Datum a čas kdy k události došlo. Pozn.: Změna počátku události posouvá celou událost v čase. Změna konce události ovlivňuje její trvání.',
        'occurrence.end-time': 'Konec události',
        'occurrence.end-time-tooltip': 'Datum a čas kdy událost skončila',
        'occurrence.class': 'Třída závažnosti',
        'occurrence.class-tooltip': 'Třída závažnosti - pole je povinné',
        'occurrence.add-category': 'Přidat další klasifikaci události',
        'occurrence.remove-category-tooltip': 'Odebrat klasifikaci události',

        'safety-issue.panel.active-tooltip': 'Tato safety issue je v současné době aktivní',
        'safety-issue.panel.inactive-tooltip': 'Tato safety issue není v současné době aktivní',
        'safety-issue.name-tooltip': 'Krátké pojmenování problému - pole je povinné',
        'safety-issue.activate': 'Aktivovat',
        'safety-issue.activate-tooltip': 'Nastavit tuto safety issue jako aktivní',
        'safety-issue.deactivate': 'Deaktivovat',
        'safety-issue.deactivate-tooltip': 'Nastavit tuto safety issue jako neaktivní',
        'safety-issue.base.remove-tooltip': 'Odebrat toto hlášení z podkladů této safety issue',
        'safety-issue.base.no-bases': 'Tato safety issue není založena na základě žádných existujících hlášení.',
        'safety-issue.sira.label': 'Hodnocení rizika safety issue (SIRA)',
        'safety-issue.sira-tooltip': 'SIRA, dle https://essi.easa.europa.eu/documents/Methodology.pdf',
        'safety-issue.sira.initialEventFrequency': 'Četnost výskytu spouštějící události',
        'safety-issue.sira.initialEventFrequency-tooltip': 'Odhadovaná četnost výskytu spouštějící události (ku letovým sektorům):',
        'safety-issue.sira.barrierUosAvoidanceFailFrequency': 'Četnost selhání bariér proti UOS',
        'safety-issue.sira.barrierUosAvoidanceFailFrequency-tooltip': 'Bariéry selžou při prevenci UOS (Undesirable Operational State)...',
        'safety-issue.sira.barrierRecoveryFailFrequency': 'Četnost selhání bariér před nehodou',
        'safety-issue.sira.barrierRecoveryFailFrequency-tooltip': 'Bariéry nedokáží zabránit nehodové situaci...',
        'safety-issue.sira.accidentSeverity': 'Závažnost nehody',
        'safety-issue.sira.accidentSeverity-tooltip': 'Závažnost nehody by byla...',
        'safety-issue.sira.value-label': 'Hodnota SIRA',
        'safety-issue.sira.reset': 'Resetovat SIRA',
        'safety-issue.sira.reset-tooltip': 'Zrušit SIRA hodnocení',

        'audit.name-tooltip': 'Název auditu (např. jméno auditované organizace + datum) - pole je povinné',
        'audit.type.label': 'Druh kontroly',
        'audit.type.placeholder': 'Vyberte druh kontroly',
        'audit.auditee.label': 'Kontrolované organizace',
        'audit.auditee.placeholder': 'Vyberte kontrolovanou organizaci',
        'audit.auditor.label': 'Auditor',
        'audit.auditor.tooltip': 'Organizace, která audit prováděla',
        'audit.location.label': 'Místo kontroly',
        'audit.location.placeholder': 'Vyberte místo kontroly',
        'audit.start-date': 'Začátek kontroly',
        'audit.start-date.tooltip': 'Datum a čas prvního kontrolního úkonu',
        'audit.end-date': 'Konec kontroly',
        'audit.end-date.tooltip': 'Datum a čas posledního kontrolního úkonu',
        'audit.remarks': 'Poznámky',
        'audit.remarks.placeholder': 'Poznámky k auditu',
        'audit.findings.header': 'Kontrolní zjištění',
        'audit.findings.no-findings-message': 'Nebyla zadána žádná kontrolní zjištění.',
        'audit.findings.table.description': 'Popis nálezu',
        'audit.findings.table.type': 'Typ nálezu',
        'audit.findings.table.level': 'Úroveň nálezu',
        'audit.findings.table.open-tooltip': 'Zobrazit a editovat toto zjištění',
        'audit.findings.table.delete-tooltip': 'Odstranit toto zjištění',
        'audit.findings.add-tooltip': 'Přidat nové zjištění',
        'audit.finding.header': 'Kontrolní zjištění',
        'audit.finding.type.label': 'Typ zjištění',
        'audit.finding.type.placeholder': 'Vyberte typ zjištění',
        'audit.finding.type.edit-tooltip': 'Klikněte zde pro editaci typu zjištění',
        'audit.finding.level.label': 'Úroveň nálezu',
        'audit.finding.level': 'Nález úrovně {level}',
        'audit.finding.level-1.help': 'A level 1 finding shall be issued by the competent authority when any significant noncompliance '
        + 'is detected with the applicable requirements of Regulation (EC) No 216/2008 and '
        + 'its Implementing Rules, with the organisation’s procedures and manuals or with the terms of '
        + 'an approval or certificate or with the content of a declaration which lowers safety or seriously '
        + 'hazards flight safety. '
        + 'The level 1 findings shall include: '
        + '(1) failure to give the competent authority access to the organisation\'s facilities as defined '
        + 'in ORO.GEN.140 during normal operating hours and after two written requests; '
        + '(2) obtaining or maintaining the validity of the organisation certificate by falsification of '
        + 'submitted documentary evidence; '
        + '(3) evidence of malpractice or fraudulent use of the organisation certificate; and '
        + '(4) the lack of an accountable manager.',
        'audit.finding.level-2.help': 'A level 2 finding shall be issued by the competent authority when any non-compliance is '
        + 'detected with the applicable requirements of Regulation (EC) No 216/2008 and its '
        + 'Implementing Rules, with the organisation\'s procedures and manuals or with the terms of an '
        + 'approval or certificate or with the content of a declaration which could lower safety or hazard '
        + 'flight safety.',
        'audit.finding.description-tooltip': 'Popis zjištěného nálezu',
        'audit.finding.factors': 'Faktory nálezu',
        'audit.finding.factors.placeholder': 'Typ faktoru',
        'audit.finding.factors.remove-tooltip': 'Klikněte pro odstranení faktoru',
        'audit.finding.measures.no-measures-message': 'Nebyla definována žádná nápravná opatření',
        'audit.finding.measures.description': 'Popis opatření',
        'audit.finding.measures.deadline': 'Termín zavedení',
        'audit.finding.measures.implemented': 'Bylo zavedeno?',
        'audit.finding.measures.implemented.yes': 'Nápravné opatření bylo zavedeno',
        'audit.finding.measures.implemented.no': 'Nápravné opatření ještě nebylo zavedeno',
        'audit.finding.measures.open-tooltip': 'Zobrazit a editovat toto nápravné opatření',
        'audit.finding.measures.delete-tooltip': 'Odstranit toto nápravné opatření',
        'audit.finding.measures.add.select-existing': 'Vybrat existující',
        'audit.finding.measures.add.select-existing-tooltip': 'Vybrat nápravné opatření použité u jiného nálezu v tomto auditu',
        'audit.finding.measures.add.create-new': 'Vytvořit nové',
        'audit.finding.measures.add.create-new-tooltip': 'Vytvořit nové nápravné opatření',
        'audit.safa.readonly.message': 'Zprávy ze SAFA auditů jsou pouze pro čtení.',
        'audit.finding.status.label': 'Stav',
        'audit.finding.status.tooltip': 'Stav tohoto nálezu',
        'audit.finding.status.lastModified': 'Status nálezu naposledy upraven {date}.',

        'correctivemeasure.title': 'Nápravné opatření',
        'correctivemeasure.description.placeholder': 'Popis nápravného opatření',
        'correctivemeasure.description.tooltip': 'Popis nápravného opatření - pole je povinné',
        'correctivemeasure.deadline': 'Nejpozdější termín zavedení',
        'correctivemeasure.deadline.tooltip': 'Datum, do kdy by opatření mělo být zavedeno',

        'initial.panel-title': 'Prvotní hlášení',
        'initial.table-report': 'Hlášení',
        'initial.wizard.add-title': 'Přidat prvotní hlášení',
        'initial.wizard.edit-title': 'Editovat prvotní hlášení',
        'initial.label': 'Prvotní hlášení',
        'initial.tooltip': 'Text prvotního hlášení - pole je povinné',

        'report.summary': 'Shrnutí hlášení',
        'report.created-by-msg': 'Vytvořil(a) {name} {date}.',
        'report.last-edited-msg': 'Naposledy upravil(a) {name} {date}.',
        'report.narrative-tooltip': 'Popis - pole je povinné',
        'report.table-edit-tooltip': 'Editovat položku',
        'report.table-delete-tooltip': 'Smazat položku',
        'report.corrective.panel-title': 'Nápravná opatření',
        'report.corrective.table-description': 'Opatření',
        'report.corrective.description-placeholder': 'Popis nápravného opatření',
        'report.corrective.description-tooltip': 'Popis nápravného opatření - pole je povinné',
        'report.corrective.add-tooltip': 'Přida popis nápravného opatření',
        'report.corrective.wizard.title': 'Průvodce nápravným opatřením',
        'report.corrective.wizard.step-title': 'Popis nápravného opatření',
        'report.corrective.evaluation.title': 'Hodnocení realizace',
        'report.corrective.evaluation.evaluation': 'Hodnocení',
        'report.corrective.evaluation.evaluation-notes': 'Poznámky k hodnocení',
        'report.eventtype.table-type': 'Typ události',
        'report.eventtype.add-tooltip': 'Přidat popis typu události',
        'report.organization': 'Organizace',
        'report.responsible-department': 'Zodpovědné oddělení',
        'report.responsible-department.add-tooltip': 'Přidat další zodpovědné oddělení',
        'report.attachments.title': 'Přílohy',
        'report.attachments.create.button': 'Přiložit',
        'report.attachments.create.reference-label': 'Příloha',
        'report.attachments.create.reference-tooltip': 'Příloha, např. adresa dokumentu - pole je povinné',
        'report.attachments.create.description-label': 'Popis',
        'report.attachments.create.description-tooltip': 'Volitelný popis přílohy',
        'report.attachments.table.reference': 'Příloha',
        'report.eccairs.button.label': 'ECCAIRS hlášení',
        'report.eccairs.button.tooltip': 'Zobrazit nejnovější ECCAIRS hlášení pro toto hlášení',
        'report.eccairs.error-msg': 'Nepodařilo se najít nejnovější ECCAIRS hlášení pro toto hlášení.',
        'report.eccairs.create-new-revision.label': 'Nová revize z ECCAIRS',
        'report.eccairs.create-new-revision.tooltip': 'Vytvořit novou revizi tohoto hlášení podle nejnovější verze v ECCAIRS',
        'report.eccairs.create-new-revision.success': 'Nová revize hlášení podle nejnovější verze v ECCAIRS úspěšně vytvořena.',
        'report.eccairs.create-new-revision.error': 'Nepodařilo se vytvořit novou revizi hlášení podle nejnovější verze v ECCAIRS. Odpověď serveru: ',
        'report.eccairs.label': 'ECCAIRS',
        'report.safa.label': 'RAMP',
        'report.summary.button.title': 'Zobrazit přehled hlášení',
        'report.summary.button.title-invalid': 'Přehled není dostupný pro nevalidní hlášení',

        'report.occurrence.category.label': 'Klasifikace události',
        'occurrencereport.title': 'Hlášení o události',
        'occurrencereport.label': 'Událost',
        'occurrencereport.create-safety-issue': 'Vytvořit safety issue',
        'occurrencereport.create-safety-issue-tooltip': 'Vytvořit safety issue na základě tohoto hlášení',
        'occurrencereport.add-as-safety-issue-base': 'Přidat jako základ safety issue',
        'occurrencereport.add-as-safety-issue-base-tooltip': 'Přidat toto hlášení jako základ safety issue',
        'occurrencereport.add-as-safety-issue-base-placeholder': 'Jméno safety issue',
        'safetyissuereport.title': 'Hlášení o safety issue',
        'safetyissuereport.label': 'Safety issue',
        'safetyissue.based-on': 'Vytvořeno na základě',
        'safetyissue.base-add-success': 'Podklad safety issue úspěšně přidán. Změny uložíte kliknutím na tlačítko \'Uložit\'.',
        'safetyissue.base-add-duplicate': 'Safety issue již je založeno na daném hlášení.',
        'auditreport.title': 'Hlášení o auditu',
        'auditreport.label': 'Audit',

        'wizard.finish': 'Dokončit',
        'wizard.next': 'Další',
        'wizard.previous': 'Předchozí',
        'wizard.advance-disabled-tooltip': 'Některá povinná pole nejsou vyplněna',

        'eventtype.title': 'Typ události',
        'eventtype.default.description': 'Popis',
        'eventtype.default.description-placeholder': 'Popis události',

        'factors.panel-title': 'Faktory',
        'factors.scale': 'Měřítko',
        'factors.scale-tooltip': 'Kliknutím vyberete měřítko: ',
        'factors.scale.second': 'Sekundy',
        'factors.scale.minute': 'Minuty',
        'factors.scale.hour': 'Hodiny',
        'factors.scale.relative': 'Relativní',
        'factors.scale.relative-tooltip': 'Kliknutím vyberete relativní měřítko',
        'factors.link-type-select': 'Typ vztahu mezi faktory?',
        'factors.link-type-select-tooltip': 'Vyberte typ vztahu',
        'factors.link.delete.title': 'Smazat link?',
        'factors.link.delete.text': 'Určitě chcete smazat spojení vedoucí z faktoru {source} do faktoru {target}?',
        'factors.event.label': 'Událost',
        'factors.detail.title': 'Specifikace faktoru',
        'factors.detail.type': 'Typ faktoru',
        'factors.detail.type-placeholder': 'Typ faktoru',
        'factors.detail.time-period': 'Specifikace času',
        'factors.detail.start': 'Faktor nastal',
        'factors.detail.duration': 'Trvání',
        'factors.duration.second': '{duration, plural, =0 {sekund} one {sekunda} few {sekundy} other {sekund}}',
        'factors.duration.minute': '{duration, plural, =0 {minut} one {minuta} few {minuty} other {minut}}',
        'factors.duration.hour': '{duration, plural, =0 {hodin} one {hodina} few {hodiny} other {hodin}}',
        'factors.detail.details': 'Detail faktoru',
        'factors.detail.delete.title': 'Smazat faktor?',
        'factors.detail.delete.text': 'Určitě chcete smazat tento faktor?',
        'factors.detail.wizard-loading': 'Připravuji formulář...',
        'factors.detail.wizard.button.tooltip': 'Zobrazit či upravit detaily tohoto faktoru',
        'factors.detail.wizard.button-invalid.tooltip': 'Detaily faktoru nejsou dostupné pro faktory bez typu a pro nevalidní hlášení',

        'notfound.title': 'Nenalezeno',
        'notfound.msg-with-id': 'Záznam \'{resource}\' s identifikátorem {identifier} nenalezen.',
        'notfound.msg': 'Záznam \'{resource}\' nenalezen.',

        'notrenderable.title': 'Nelze zobrazit záznam',
        'notrenderable.error': 'Chyba: {message}',
        'notrenderable.error-generic': 'Zkontrolujte, prosím, zda je záznam validní.',

        'revisions.label': 'Revize hlášení',
        'revisions.created': 'Vytvořeno',
        'revisions.show-tooltip': 'Zobrazit tuto revizi',
        'revisions.readonly-notice': 'Starší revize jsou pouze ke čtení.',

        'sort.no': 'Kliknutím seřadíte záznamy podle tohoto sloupce',
        'sort.asc': 'Záznamy jsou seřazeny vzestupně',
        'sort.desc': 'Záznamy jsou seřazeny sestupně',

        // ----------- ARMS -------------
        'arms.title': 'ARMS',
        'arms.accident-outcome': 'Následky nehody',
        'arms.accident-outcome.tooltip': 'Pokud by tato událost vedla až nehodě, jaký by byl její nejpravděpodobnější výsledek?',
        'arms.barrier-effectiveness': 'Efektivita bariér',
        'arms.barrier-effectiveness.tooltip': 'Jaká byla efektivita bariér zbývajících mezi touto událostí a nejpravděpodobnějším nehodovým scénářem?',
        'arms.index': 'ARMS index',
        'arms.index.tooltip': 'ARMS index je ',

        'statistics.type.general': 'Obecné',
        'statistics.type.eventTypes': 'Typy událostí',
        'statistics.type.audit': 'Audity',
        'statistics.type.safetyIssue': 'Safety issue',
        'statistics.type.sag': 'SAG',

        'search.loading': 'Probíhá hledání...',
        'search.title': 'Výsledky hledání',
        'search.headline': 'Nalezeno {count, plural, one {# výskyt} few {# výskyty} other {# výskytů}} výrazu {expression}.',
        'search.results.match': 'Výsledek hledání'
    }
};