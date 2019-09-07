# os3_home_alarm

OS3 Deeltijd huis alarm project

Contributors:

Bas
Wesley
Melvin
Lucas

Het project betreft een applicatie die de mogelijkheid biedt om meerdere huizen te beveiligen d.m.v. de Arduino Uno. Op elementair niveau bestaat het systeem uit drie componenten: De arduino + laptop, de server, en de gebruiker. De gebruiker zal zijn huis kunnen beheren via zijn pc en wordt gemeld via SMS als er een inbraak is. Mogelijke uitbreidingen voor het systeem zijn bijvoorbeeld het nabootsen van een realitische situatie met x aantal huizen, een hoofdbeheerder, en een videocamera / microfoon die de inbreker kan identificieren.

Betreft branches en development:

Ieder heeft zijn eigen branch om features op te bouwen, in principe kan je hier unfinished / niet werkende code op committen. Wanneer je een werkende feature hebt kan je branch gemerged worden met de dev branch. Bij het mergen verdwijnt je branch. Je zal dus een nieuwe branch moeten maken na elke feature die je bouwt.

Git branch commands:
git branch <naam_van_jou_nieuwe_branch> ex. git branch lucas
git checkout <bestaande_branch> ex. git checkout lucas

Git merge:

Als je wilt mergen met bijv. de development branch zal je eerst op de dev branch moeten gaan:

git checkout development
git merge <branch_naam> ex. git merge lucas

Git commit:

Elke verandering die je maakt op je locale repository word getraceerd door git. Gebruik git status om te zien hoe jou huidige versie er uit ziet tegenover de laatste commit. Hierna kan je een individueel bestand op de staging area toevoegen met git add <filename> of alle bestanden met git add .
  
Om deze veranderingen te committen gebruik git commit -m 'commitbericht'

ex.

git status
git add README.md
git commit -m 'wijziging van de readme file'

Zie codecademy git tutorial voor meer. Leer hoe je hier om mee moet gaan want je houdt de rest alleen maar op als je de basics van git niet beheerst.


De dev branch moet dus altijd werken, hiermee preventeren we dat iedereen z'n eigen deel maakt en dat het niet te integreren valt.

Op de master branch zullen de prototypes komen.


