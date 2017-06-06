function [mDeskriptiveStatistik, cDeskriptiveStatistik]=fDeskriptiveStatistik(mDaten, cVarNamen)
% Funktion fDeskriptiveStatistik
% Berechnete die deskriptiven Statistiken (in dieser Reihenfolge in den
% Zeilen angeordnet): Median, Mittelwert, Unteres Quartil, Oberes
% Quartil, Maximum, Minimum, Spannweite, Interquartilsbereich
% (Quartilsabstand zwischem dem 1. - 25% und dem 3. - 75 % Quartil),
% Varianz, Standardabweichung, Anzahl Beobachtungen
% @author: Benjamin M. Abdel-Karim
% @Since 2027-05-15
% @Version 2017-05-11
% @Entwichlungszeit: 5h
% Aufruf:
%   [ cDescriptiveStats, mDescriptiveStats ] = ...
%       fDescriptiveStats(mData)
%
% Input:
%  mDaten:     T x K Datenmatrix mit T Beobachtungsdaten fuer K Objekte
%  Achtung die Zeitreihen muessen Spaltenweise vorlegen.
%
% Output:
%  cDeskriptiveStatistik: 9 x (K+1) Cell-Array, Kennzahlenwerte mit
%              entsprechender Beschriftung
%  mDeskriptiveStatistik: 9 x K Matrix, aufgebaut wie , cDescriptiveStats nur 
%              keine Zeilen- und Spaltenbeschriftungen.

iAnzahlDerSpalten = size(mDaten,2);

% Wenn keine Spaltenbeschriftungen gelierfert wird, dann automatisch
% beschriften mit Nr. und der Spaltennummer.
if nargin < 2 || isempty(cVarNamen)
    for iIndex = 1: iAnzahlDerSpalten
        cVarNamen(1,iIndex) = {strcat('Nr.', num2str(iIndex))};
    end
end

% Leere Ergebnismatrix generieren
mDeskriptiveStatistik = NaN(9,iAnzahlDerSpalten);

% Mittelwerte berechnen
mDeskriptiveStatistik(1,:) = mean(mDaten);

% Mediane berechnen
mDeskriptiveStatistik(2,:) = median(mDaten);

% Varianz und Standardabweichung berechnen, hier ist die Option
% 1 gesetzt, da beschreibende Statistiken berechnet werden sollen
mDeskriptiveStatistik(3,:) = var(mDaten,1);
mDeskriptiveStatistik(4,:) = std(mDaten,1);

% Minimum, Maximum und Spannweite
mDeskriptiveStatistik(5,:) = min(mDaten);
mDeskriptiveStatistik(6,:) = max(mDaten);
mDeskriptiveStatistik(7,:) = max(mDaten) - min(mDaten);

% Interquartile-Range
mDeskriptiveStatistik(8,:) = quantile(mDaten,0.75) - quantile(mDaten,0.25);
mDeskriptiveStatistik(9,:) = length(mDaten);
% Leeres Cell-Array fuer Zeilenbeschriftungen generieren
cLabel = cell(8,1);

% Zeilenbeschriftungen
cLabel{1,1} = 'Mittelwert';
cLabel{2,1} = 'Median';
cLabel{3,1} = 'Varianz';
cLabel{4,1} = 'Standardabweichung';
cLabel{5,1} = 'Minimum';
cLabel{6,1} = 'Maximum';
cLabel{7,1} = 'Spannweite';
cLabel{8,1} = 'IQR';
cLabel{9,1} = 'Anzahl Beobachtungen';

% Beschriftete Tabelle erstellen
cDeskriptiveStatistik = ...
    [[{'Kennzahl'};cLabel], [cVarNamen; num2cell(mDeskriptiveStatistik)]];

end