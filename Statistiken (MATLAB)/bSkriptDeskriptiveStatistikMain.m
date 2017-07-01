%% bSkriptDeskriptiveStatistikMain
% Dieses Skript realisiert die deskripitive Beschreibung des Datensatzes.
% @Author: Benjamin M. Abdel-Karim
% @since: 2017-05-30
% @version: 2017-05-30
clear; clc; close all;

%% Korrekten Pfad zu den Unterfunktionen legen
addpath('Funktionsbibliothek');
addpath('Funktionsbibliothek/MATLAB2Tikiz/src');

%% Dataimport 
% Der Datenimport aus der Datei.
% Aufbau der Tabelle: 
sVar = {'$X_{1}$ words','$X_{2}$ uppercases','$X_{3}$ questions',...
    '$X_{4}$ exclamations','$X_{5}$ authors','$X_{6}$ citations',...
    '$X_{7}$ firstperson','$X_{8}$ secondperson', '$X_{9} $thirdperson',...
    '$X_{10}$ sentencelength','$X_{11}$ repetitiveness','$X_{12}$ authorHits','$X_{13}$ titleUppercase',...
    '$X_{14}$ errorLevel','$X_{15}$ sentiment','$X_{16}$ informativeness','$X_{17}$ super. per Words ','$X_{18}$ super. per Adj.'...
      '$X_{19}$usedsources', '$X_{20}$internsources','$X_{21}$externsources','$X_{22}$usedimages'};
   

mData = csvread('Datenbank/2017-06-20-newsResults.csv');

%% Datensatzaufteilung
lIsFake =  mData(:,2)== 1;
mDataIsFake = mData(lIsFake,:);
mDataIsNotFake = mData(~ lIsFake,:); % ~ = Ist Keine FakeNews


%% Deskriptive Statistik genierien
% Uebergabe der Daten in die Statistik Funktion
[mDeskriptiveStatistik, cDeskriptiveStatistik]=fDeskriptiveStatistik(mData(:,3:end),sVar);


TDeskriptiveStatistik = cell2table(cDeskriptiveStatistik');
% Save the Data @code 'WriteRowNames',1 right down the Values
writetable(TDeskriptiveStatistik,'Datenexporte/DeskriptiveStatistikT.csv','WriteRowNames',1);




% Old Protocolls - Save the new Data in a file
%dlmwrite('Datenexporte/DeskriptiveStatistik.txt',mDeskriptiveStatistik);
%csvwrite('Datenexporte/DeskriptiveStatistik.csv',mDeskriptiveStatistik);
%csvwrite('Datenexporte/DeskriptiveStatistikTranspost.csv',mDeskriptiveStatistik');
%dlmwrite('Datenexporte/DeskriptiveStatistikTranspost.csv',mDeskriptiveStatistik','delimiter',',','precision','%.2f');

%% Auswertung ist Fake
% Aus dem Datenpaket die relevanten Informationen ziehen
vIsFake = mData(:,2);

% Identifizieren welcher Eintrag Fake ist und welcher nicht!
% Besondere Funktion fuer den Pie-Chart
vIsFakeTrue = vIsFake(vIsFake==1);
vIsFakeFale = vIsFake(vIsFake==0);
vIsFakeFale(vIsFakeFale==0)=1;% Schreibe fuer jede Null eine Eins.

% Berechnung der Gesamtanzahl
dSumIsFakeTrue = sum(vIsFakeTrue);
dSumIsFakeFale = sum(vIsFakeFale);

% Zusammenfassung der Information
dVerteilungIsFakeandNot = [dSumIsFakeTrue, dSumIsFakeFale];

% Abbildung mit Beschriftung wird erstellt und als TikZ gespeichert.
figure;
pie(dVerteilungIsFakeandNot);
title('Verteilung der Nachrichten zwischen falschen und wahren Nachrichten');
legend('wahr', 'falsch');
%matlab2tikz('Abbildungen/VerteilungIsFakeNews.tex');
% print -dpdf Abbildungen/VerteilungIsFakeNews.pdf;


%% VerteilungDerWoerter
% Sortierung der Datenfehler gem. Bedingungen bzgl. der Eigenschaften
vAnzahlWoeterIsFake = mDataIsFake(:,3);
vAnzahlWoeterIsNotFake = mDataIsNotFake(:,3);

% Uebereinandergelegtes Histogramm
figure
dBlocks = 50;
h1 = histogram(vAnzahlWoeterIsFake, dBlocks);
hold on
h2 = histogram(vAnzahlWoeterIsNotFake, dBlocks);
h1.Normalization = 'probability';
h2.Normalization = 'probability';
grid on;
legend('Fake News','Keine Fake News');
xlabel('Wortanzahl in einer Nachricht');
ylabel('Relativer Anteil');
title('Wortanzahl in Fake und nicht Fake News');
%matlab2tikz('Abbildungen/VerteilungIsFakeNewsAndNotFake.tex');
% print -dpdf Abbildungen/VerteilungIsFakeNewsAndNotFake.pdf;

% Alternative Darstellung
figure;
subplot(2,1,1);
dBlocks = 20;
histogram(vAnzahlWoeterIsFake, dBlocks,'FaceColor',[1 1 1]);
legend('Fake News');
xlabel('Verteilung');
ylabel('Anzahl');
title('Wortanzahl in den falschen Nachrichten');
xlim([0 2000]);
ylim([0 40]);
grid on;
subplot(2,1,2);
histogram(vAnzahlWoeterIsNotFake, dBlocks, 'FaceColor',[0 0 0]); % Wei�e Farbe
legend('Not Fake News');
xlabel('Verteilung');
ylabel('Anzahl');
title('Wortanzahl in den wahren Nachrichten');
grid on;
xlim([0 2000]);
ylim([0 40]);
%matlab2tikz('Abbildungen/VerteilungIsFakeNewsSubplotHistogramm.tex');
% print -dpdf Abbildung/Wortverteilung.pdf;

%% VerteilungDerWoerter
% Sortierung der Datenfehler gem. Bedingungen bzgl. der Eigenschaften
vSourceIsFake = mDataIsFake(:,21:24);
vSourceIsNotFake = mDataIsNotFake(:,21:24);

% 'FaceAlpha', 0.9, 'FaceColor',[0 0 0]); Farbe und Durchsichtigkeit 
figure;
subplot(2,1,1);
histogram(vSourceIsFake(:,1),'FaceAlpha', 0.4, 'FaceColor',[0 0 0]);
hold on;
histogram(vSourceIsFake(:,2),'FaceAlpha', 0.3, 'FaceColor',[0 0 0]);
hold on; 
histogram(vSourceIsFake(:,3),'FaceAlpha', 0.2, 'FaceColor',[0 0 0]);
hold on;
histogram(vSourceIsFake(:,4),'FaceAlpha', 0.1, 'FaceColor',[0 0 0]);
legend('usedsources','internsources','externsources','usedimages');
xlabel('Verteilung');
ylabel('Anzahl');
title('Quellen in falschen Nachrichten');
grid on;

subplot(2,1,2);
histogram(vSourceIsNotFake(:,1),'FaceAlpha', 0.4, 'FaceColor',[0 0 0]);
hold on;
histogram(vSourceIsNotFake(:,2),'FaceAlpha', 0.3, 'FaceColor',[0 0 0]);
hold on; 
histogram(vSourceIsNotFake(:,3),'FaceAlpha', 0.2, 'FaceColor',[0 0 0]);
hold on;
histogram(vSourceIsNotFake(:,4),'FaceAlpha', 0.1, 'FaceColor',[0 0 0]);
legend('usedsources','internsources','externsources','usedimages');
xlabel('Verteilung');
ylabel('Anzahl');
title('Quellen in wahren Nachrichten');
grid on;
matlab2tikz('Abbildungen/VerteilungQuellen.tex');
% print -dpdf Abbildung/Wortverteilung.pdf;

figure;
ribbon(vSourceIsFake);
xticklabels({'','usedsources','internsources','externsources','usedimages',''});
view([39 26])
colormap('gray')
title('Quellen in Fake News');
grid on;
matlab2tikz('Abbildungen/QuellenFakeNews.tex');

figure;
ribbon(vSourceIsNotFake);
xticklabels({'','usedsources','internsources','externsources','usedimages',''});
view([39 26])
colormap('gray')
title('Quellen in den Not Fake News');
grid on;
matlab2tikz('Abbildungen/QuellenNotFakeNews.tex');

%% Boxplotverteilung

%vQuestionsmarkIsFake = mDataIsFake(:,5);
%vQuestionsmarkIsNotFake = mDataIsNotFake(:,5);
%histogram(mDataIsFake(:,5)); % Die Fragezeichen

