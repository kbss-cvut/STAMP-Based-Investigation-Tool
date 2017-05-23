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
        'issue-fix': 'Opravit problém',

        'detail.save-tooltip': 'Uložit změny',
        'detail.saving': 'Ukládám...',
        'detail.invalid-tooltip': 'Některá povinná pole nejsou vyplněna',
        'detail.large-time-diff-tooltip': 'Časový rozdíl počátku a konce události je příliš velký',
        'detail.large-time-diff-event-tooltip': 'Časový rozdíl mezi událostí a jejími podudálostmi je příliš velký',
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
        'dashboard.create-tile': 'Vytvořit hlášení',
        'dashboard.search-tile': 'Hledat hlášení',
        'dashboard.search-placeholder': 'Název hlášení',
        'dashboard.view-all-tile': 'Prohlížet všechna hlášení',
        'dashboard.recent-panel-heading': 'Nedávno přidaná/upravená hlášení',
        'dashboard.recent-table-last-edited': 'Naposledy upraveno',
        'dashboard.recent.no-reports': 'Zatím nebylo vytvořeno žádné hlášení.',
        'dashboard.import-initial-tile': 'Importovat hlášení',

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
        'reports.unable-to-load': 'Hlášení se nepodařilo načíst. Chybová konzole prohlížeče obsahuje podrobnější informace.',

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

        'report.initial.import.title': 'Import prvotního hlášení',
        'report.initial.import.run': 'Importovat',
        'report.initial.import.text.tooltip': 'Vložte text prvotního hlášení',
        'report.initial.import.importing-msg': 'Probíhá analýza hlášení',
        'report.initial.text.label': 'Text',
        'report.initial.label': 'Prvotní hlášení',
        'report.initial.view.tooltip': 'Zobrazit prvotní hlášení',
        'report.initial.analysis-results.label': 'Výsledky analýzy hlášení',

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
        'report.eventtype.table-type': 'Typ události',
        'report.eventtype.add-tooltip': 'Přidat popis typu události',
        'report.organization': 'Organizace',
        'report.responsible-department': 'Zodpovědné oddělení',
        'report.attachments.title': 'Přílohy',
        'report.attachments.create.button': 'Přiložit',
        'report.attachments.create.reference-label': 'Příloha',
        'report.attachments.create.reference-tooltip': 'Příloha, např. adresa dokumentu - pole je povinné',
        'report.attachments.create.description-label': 'Popis',
        'report.attachments.create.description-tooltip': 'Volitelný popis přílohy',
        'report.attachments.table.reference': 'Příloha',

        'report.occurrence.category.label': 'Klasifikace události',
        'occurrencereport.title': 'Hlášení o události',
        'occurrencereport.label': 'Událost',

        'wizard.finish': 'Dokončit',
        'wizard.next': 'Další',
        'wizard.previous': 'Předchozí',
        'wizard.advance-disabled-tooltip': 'Některá povinná pole nejsou vyplněna',

        'eventtype.title': 'Typ události',
        'eventtype.default.description': 'Popis',
        'eventtype.default.description-placeholder': 'Popis události',

        'factors.panel-title': 'Faktory',
        'factors.scale': 'Měřítko',
        'factors.scale-tooltip': 'Kliknutím vyberete měřítko: {unit}',
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
        'factors.smallscreen.start': 'Počátek',
        'factors.smallscreen.end': 'Konec',
        'factors.smallscreen.add-tooltip': 'Přidat událost',
        'factors.event-suggested': 'Faktor navržen na základě analýzy prvotního hlášení.',

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

        'search.loading': 'Probíhá hledání...',
        'search.title': 'Výsledky hledání',
        'search.headline': '{count, plural, one {Nalezen # výskyt} few {Nalezeny # výskyty} other {Nalezeno # výskytů}} výrazu {expression}.',
        'search.results.match': 'Výsledek hledání',

        'validation.error.start-after-end': 'Chyba: konec nemůže nastat před počátkem.',

        'editor.rich.h1': 'Nadpis 1',
        'editor.rich.h2': 'Nadpis 2',
        'editor.rich.h3': 'Nadpis 3',
        'editor.rich.h4': 'Nadpis 4',
        'editor.rich.h5': 'Nadpis 5',
        'editor.rich.h6': 'Nadpis 6',
        'editor.rich.body': 'Text',
        'editor.rich.ul': 'Odrážky',
        'editor.rich.ol': 'Číslovaný seznam',
        'editor.rich.blockquote': 'Citace',
        'editor.rich.bold': 'Tučné',
        'editor.rich.italic': 'Kurzíva',
        'editor.rich.underline': 'Podtržené'
    }
};