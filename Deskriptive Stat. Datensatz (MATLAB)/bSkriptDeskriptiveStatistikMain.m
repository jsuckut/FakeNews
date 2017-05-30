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
sVar = {'newsId','isFake','words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits'};
mData = csvread('Datenbank/2017-05-30-newsResults.csv');

%% Datensatzaufteilung
lIsFake =  mData(:,2)== 1;
mDataIsFake = mData(lIsFake,:);
mDataIsNotFake = mData(~ lIsFake,:);
% mDataIsNotFake = mData(mData(~lIsFake),:); % ~ = Ist Keine FakeNews

%% Deskriptive Statistik genierien
% Uebergabe der Daten in die Statistik Funktion
[mDeskriptiveStatistik, cDeskriptiveStatistik]=fDeskriptiveStatistik(mData,sVar);

% Save the new Data in a file
dlmwrite('Datenexporte/DeskriptiveStatistik.txt',mDeskriptiveStatistik);
csvwrite('Datenexporte/DeskriptiveStatistik.csv',mDeskriptiveStatistik);
csvwrite('Datenexporte/DeskriptiveStatistikTranspost.csv',mDeskriptiveStatistik');

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

% Zusammenfassung dr Information
dVerteilungIsFakeandNot = [dSumIsFakeTrue, dSumIsFakeFale];

% Abbildung mit Beschriftung wird erstellt und als TikZ gespeichert.
figure;
pie(dVerteilungIsFakeandNot);
title('Verteilung der Nachrichten zwischen falschen und wahren Nachrichten');
legend('wahr', 'falsch');
% matlab2tikz('Abbildungen/VerteilungIsFakeNews.tex');
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
% matlab2tikz('Abbildungen/VerteilungIsFakeNewsAndNotFake.tex');
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
histogram(vAnzahlWoeterIsNotFake, dBlocks, 'FaceColor',[0 0 0]); % Weiﬂe Farbe
legend('Not Fake News');
xlabel('Verteilung');
ylabel('Anzahl');
title('Wortanzahl in den wahren Nachrichten');
grid on;
xlim([0 2000]);
ylim([0 40]);
% matlab2tikz('Abbildungen/VerteilungIsFakeNewsSubplotHistogramm.tex');
% print -dpdf Abbildung/Wortverteilung.pdf;

%% Boxplotverteilung

%vQuestionsmarkIsFake = mDataIsFake(:,5);
%vQuestionsmarkIsNotFake = mDataIsNotFake(:,5);
%histogram(mDataIsFake(:,5)); % Die Fragezeichen

