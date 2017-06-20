%% bSkript_MultinomialLogisticRegression
% This script implements the multinomial Logistic Regression
% This script uses the CamelCase spelling and the following conventions:
% m = Matrix
% v = vector
% s = String
% c = Cell-Array
% @source: https://de.mathworks.com/help/stats/mnrfit.html
% @author: Benjamin M. Abdel-Karim
% @since: 2017-06-06
% version 2017-06-19

clear; clc; close all;
addpath('Funktionsbibliothek/MATLAB2Tikiz/src');
addpath('Funktionsbibliothek');

%% Import the actual data
% Loading the data from sql Database csv export
% Each vector in the matrix stands for the input:
% 0 = 'newsId'
% 0 = 'isFake'
% 1 = 'words'
% 2 = 'uppercases'
% 3 = 'questions'
% 4 = 'exclamations'
% 5 = 'authors'
% 6 = 'citations'
% 7 = 'firstperson'
% 8 = 'secondperson'
% 9 = 'thirdperson'
% 10 = 'sentencelength'
% 11 = 'repetitiveness'
% 12 = 'authorHits'
% 13 = 'titleUppercase'
% 14 = 'errorLevel'
% 15 = 'sentiment'
% 16 = 'informativeness'
% 17 = 'superlativesPerWords'
% 18 = 'superlativesPerAdjectives'
% 19 = 'usedsources'
% 20 = 'internsources'
% 21 = 'externsources'
% 22 = 'usedimages'
sVar = {'$X_{1}$ words','$X_{2}$ uppercases','$X_{3}$ questions',...
    '$X_{4}$ exclamations','$X_{5}$ authors','$X_{6}$ citations',...
    '$X_{7}$ firstperson','$X_{8}$ secondperson', '$X_{9} $thirdperson',...
    '$X_{10}$ sentencelength','$X_{11}$ repetitiveness','$X_{12}$ authorHits','$X_{13}$ titleUppercase',...
    '$X_{14}$ errorLevel','$X_{15}$ sentiment','$X_{16}$ informativeness','$X_{17}$ super. per Words ','$X_{18}$ super. per Adj.'...
      '$X_{19}$usedsources', '$X_{20}$internsources','$X_{21}$externsources','$X_{22}$usedimages'};
   
mData = csvread('Datenbank/2017-06-21-newsResults.csv');


%% Sort the data for regression
% Sorts the data according to dependent (Y) and independent variables (X)
vY = mData(:,2);
mX = mData(:,3:end);





%% Correlation coefficients
% [r, p] = corrcoef(x) Korrelationskoeffiziente und p-Werte berechnen


[mR, mP] = corrcoef(mX);

%% Calculate the model
% Use the function for the model - Old Version:
% @source: https://de.mathworks.com/help/stats/generalizedlinearmodel-class.html?searchHighlight=GeneralizedLinearModel&s_tid=doc_srchtitle
cLogistischeRegression  = GeneralizedLinearModel.fit(mX,vY,'distr','binomial');


%% Calculate the OddRatio
% source: http://bit.csc.lsu.edu/~jianhua/emrah.pdf
% @index: at 2:end, because B1 is the constant.
vBeta = table2array(cLogistischeRegression.Coefficients(1:end,1));
vOddRatio = exp(vBeta);
%dlmwrite('Datenexporte/LogistischeRegressionOddRatio.csv',vOddRatio,'delimiter',',','precision','%.2f');

%% Give the Data

% Konvenrtiere table to array (Matrix)
mLogisticRegressValues = table2array(cLogistischeRegression.Coefficients);

% Regressionswerte mit vOddRation verbinden
mLogisticRegressPlusODD = [mLogisticRegressValues, vOddRatio];

% Alles Zusammensetzen
cErsteZeile = {'Konstante'}; % Nicht mit im Standardstring der Variablen.

% Neue Zeilenbeschriftung
cZeilenBeschriftung = [cErsteZeile; sVar']; 

% Neue Spaltenbeschriftung
cSpaltenbeschriftung = {'Variable','Estimate','SE','tStat','pValue','ODD-Ratio'};

% % Die Ergebnisse in ein cell-Array umwandeln. 
cLogisticRegressValues =  num2cell(mLogisticRegressPlusODD);

% Alles Zusammensetzen:
% cSpaltenbeschriftung (;) Abschluss in der Matrix -> Liegt oben!
% cZeileBeschriftung (,) wird neben die Werte cLogisticRegressValues gesetzt
cLogisticRegressErgebnisse  = [cSpaltenbeschriftung; cZeilenBeschriftung, cLogisticRegressValues];

tLogisticRegressErgebnisse  = cell2table(cLogisticRegressErgebnisse);

writetable(tLogisticRegressErgebnisse,'Datenexporte/LogisticRegressErgebnisseODDRation.csv','WriteRowNames',1);


%% Fusion Table with Correlation and p-Values
sNamesCorrelation = {'words','uppercases','questions',...
    'exclamations','authors','citations','firstperson','secondperson','thirdperson',...
    'sentencelength','repetitiveness','authorHits','titleUppercase',...
    'errorLevel','sentiment','informativeness','superlativesPerWords ','superlativesPerAdjectives'...
      'usedsources', 'internsources','externsources','usedimages'};

% Generate the crosscorrelation Matrix with p-Values...
mRAndMP = [mR(1,:);
    mP(1,:)
    
    mR(2,:);
    mP(2,:)
    
    mR(3,:);
    mP(3,:)
    
    mR(4,:);
    mP(4,:)
    
    mR(5,:);
    mP(5,:)
    
    mR(6,:);
    mP(6,:)
    
    mR(7,:);
    mP(7,:)
    
    mR(8,:);
    mP(8,:)
    
    mR(9,:);
    mP(9,:)
    
    mR(10,:);
    mP(10,:)
    
    mR(11,:);
    mP(11,:)
    
    mR(12,:);
    mP(12,:)
    
    mR(13,:);
    mP(13,:)
    
    mR(14,:);
    mP(14,:)
    
    mR(15,:);
    mP(15,:)
    
    mR(16,:);
    mP(16,:)
    
    mR(17,:);
    mP(17,:)
    
    mR(18,:);
    mP(18,:)
    
    mR(19,:);
    mP(19,:)
    
    mR(20,:);
    mP(20,:)
    
    mR(21,:);
    mP(21,:)
    
    mR(22,:);
    mP(22,:)
    ];

% Table 
cNamesCorrelationAndPValues = {'words','p1','uppercases','p2','questions',...
    'p3','exclamations','p4','authors','p5','citations','p6','firstperson','p7','secondperson','p8','thirdperson',...
    'p9','sentencelength','p10','repetitiveness','p11','authorHits','p12','titleUppercase',...
    'p13','errorLevel','p14','sentiment','p15','informativeness','p16','superlativesPerWords','p17','superlativesPerAdjectives','p18',...
    'usedsources','p19','internsources','p20','externsources','p21','usedimages','p22'};

tCorrcoefAndPValues = table(mRAndMP(:,1),mRAndMP(:,2), mRAndMP(:,3),mRAndMP(:,4),...
    mRAndMP(:,5), mRAndMP(:,6), mRAndMP(:,7), mRAndMP(:,8), mRAndMP(:,9), mRAndMP(:,10),...
    mRAndMP(:,11),mRAndMP(:,12),mRAndMP(:,13),mRAndMP(:,14),mRAndMP(:,15),mRAndMP(:,16),...
    mRAndMP(:,17),mRAndMP(:,18), mRAndMP(:,19), mRAndMP(:,20), mRAndMP(:,21), mRAndMP(:,22),...
    'RowNames',cNamesCorrelationAndPValues,...
    'VariableNames',{'words','uppercases','questions',...
    'exclamations','authors','citations','firstperson','secondperson','thirdperson',...
    'sentencelength','repetitiveness','authorHits','titleUppercase',...
    'errorLevel','sentiment','informativeness','superlativesPerWords ','superlativesPerAdjectives',...
    'usedsources', 'internsources','externsources','usedimages'});

writetable(tCorrcoefAndPValues,'Datenexporte/tCorrcoefAndPValues.csv','WriteRowNames',1);

% % mRAndMP Ausmessen
% [dZeilen, dSpalten] = size(mRAndMP);
% % Legt ein leeres Array fuer Strings an
% sCorrTable= int2str(zeros(dZeilen, dSpalten));
% % Verschachtelte for-Schleife die alles zusammenbaut.
% for iIndex = 1 : dSpalten
%     for jIndex = 1:dZeilen  
%         if iIndex > jIndex
%           mRAndMP(iIndex, jIndex) = 0; 
%         end
%     end
% end







%% Plot
figure;
surf(cLogistischeRegression.CoefficientCovariance);
title('Koeffizienten Kovarianz');
grid 'on';
xlabel('Variablen');
ylabel('Variablen');
view([-17 34]); % view adjust.
% Sava Data as TikZ
% matlab2tikz('Abbildungen/CoefficientCovarianceLogisticRegression.tex');
% print -dpdf Abbildungen/Wortverteilung.pdf;

%% Fisher's exact test
% Aalso returns the significance level p of the test and a structure stats
% containing additional test results, including the odds ratio and its
% asymptotic confidence interval.
% @code: https://de.mathworks.com/help/stats/fishertest.html#buh29pv-7
%[h,p,stats] = fishertest(mX,'Tail','right','Alpha',0.01);


%% Export the regression results
% Save the Date. Step one need to typecast the table object to a matrix.
% The option setting the delimiter, and the precision!
% @code: dlmwrite - For a lot of option by saving the data
% @writetable(cLogistischeRegression.Coefficients,'cLogistischeRegression.Coefficients.csv');
% @code: '%.5f' - representation of accuracy
mCoefficients = table2array(cLogistischeRegression.Coefficients);
%dlmwrite('Datenexporte/LogistischeRegressionCoefficients.csv',mCoefficients,'delimiter',',','precision','%.5f');


%% Export anthordes Values
% Save the informations as MATLAB-TABLE Object
tcorrcoef = table(mR(:,1), mR(:,2), mR(:,3), mR(:,4), mR(:,5), mR(:,6), mR(:,7),...
    mR(:,8), mR(:,9), mR(:,10), mR(:,11), mR(:,12),  mR(:,13), mR(:,14), mR(:,15),...
    mR(:,16),mR(:,17),mR(:,18), mR(:,19), mR(:,20), mR(:,21), mR(:,22),...
    'RowNames',sNamesCorrelation, 'VariableNames',{'words','uppercases','questions',...
    'exclamations','authors','citations','firstperson','secondperson','thirdperson',...
    'sentencelength','repetitiveness','authorHits','titleUppercase',...
    'errorLevel','sentiment','informativeness','superlativesPerWords ','superlativesPerAdjectives'...
      'usedsources', 'internsources','externsources','usedimages'});
  
% Save the Data @code 'WriteRowNames',1 right down the Values
%writetable(tcorrcoef,'Datenexporte/tcorrcoef.csv','WriteRowNames',1);
%writetable(tcorrcoef,'Datenexporte/tcorrcoef.xls','WriteRowNames',1);

% Save the informations as MATLAB-TABLE Object
tcorrcoef = table(mP(:,1), mP(:,2), mP(:,3), mP(:,4), mP(:,5), mP(:,6), mP(:,7),...
    mR(:,8), mR(:,9), mR(:,10), mR(:,11), mR(:,12),  mR(:,13), mR(:,14), mR(:,15),...
    mR(:,16),mR(:,17),mR(:,18), mR(:,19), mR(:,20), mR(:,21), mR(:,22),....
    'RowNames',sNamesCorrelation, 'VariableNames',{'words','uppercases','questions',...
    'exclamations','authors','citations','firstperson','secondperson','thirdperson',...
    'sentencelength','repetitiveness','authorHits','titleUppercase',...
    'errorLevel','sentiment','informativeness','superlativesPerWords ','superlativesPerAdjectives',...
      'usedsources', 'internsources','externsources','usedimages'});
% Save the Data
%writetable(tcorrcoef,'Datenexporte/tcorrcoefPValues.csv','WriteRowNames',1);
%writetable(tcorrcoef,'Datenexporte/tcorrcoefPValues.xls','WriteRowNames',1);


figure1 = figure;
axes1 = axes('Parent',figure1);
hold(axes1,'on');
heatmap(mR, sNamesCorrelation, sNamesCorrelation, '%0.5f'); %'TickAngle', 90, 'GridLines', ':'); %,'FontSize', 0.5);
set(axes1,'CLim',[-0.790076280063972 1],'Layer','top',...
    'TickLabelInterpreter','none','XTick',...
    [1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22],'XTickLabel',...
    {'words','uppercases','questions','exclamations','authors','citations',...
    'firstperson','secondperson','thirdperson','sentencelength','repetitiveness',...
    'authorHits','titleUppercase','errorLevel','sentiment','informativ',...
    'superlativesPerWords ','superlativesPerAdjectives'},...
    'XTickLabelRotation',90,'YTick',[1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22],...
    'YTickLabel',...
    {'words','uppercases','questions','exclamations','authors','citations',...
    'firstperson','secondperson','thirdperson','sentencelength','repetitiveness',...
    'authorHits','titleUppercase','errorLevel','sentiment','informativ',...
    'superlativesPerWords ','superlativesPerAdjectives',...
    'usedsources', 'internsources','externsources','usedimages'});
title('Korrelationstabelle');
%matlab2tikz('Abbildungen/Korrelationstabelle.tex');
% print -dpdf Abbildungen/Korrelationstabelle.pdf;

figure2 = figure;
axes2 = axes('Parent',figure2);
hold(axes2,'on');
heatmap(mP, sNamesCorrelation, sNamesCorrelation, '%0.4f'); %'TickAngle', 90, 'GridLines', ':'); %,'FontSize', 0.5);
set(axes2,'CLim',[-0.790076280063972 1],'Layer','top',...
    'TickLabelInterpreter','none','XTick',...
    [1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22],'XTickLabel',...
    {'words','uppercases','questions','exclamations','authors','citations',...
    'firstperson','secondperson','thirdperson','sentencelength','repetitiveness',...
    'authorHits','titleUppercase','errorLevel','sentiment','informativ',...
    'superlativesPerWords ','superlativesPerAdjectives'},...
    'XTickLabelRotation',90,'YTick',[1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22],...
    'YTickLabel',...
    {'words','uppercases','questions','exclamations','authors','citations',...
    'firstperson','secondperson','thirdperson','sentencelength','repetitiveness',...
    'authorHits','titleUppercase','errorLevel','sentiment','informativ',...
    'superlativesPerWords ','superlativesPerAdjectives',...
    'usedsources', 'internsources','externsources','usedimages'});
title('p - Werte der Korrelationstabelle');
%matlab2tikz('Abbildungen/PWerteDerKorrelationstabelle.tex');
% print -dpdf Abbildungen/PWerteDerKorrelationstabelle.pdf;
 

% figure2 = figure;
% axes2 = axes('Parent',figure2);
% hold(axes1,'on');
% heatmap(mP, sNamesCorrelation, sNamesCorrelation, '%0.4f'); %'TickAngle', 90, 'GridLines', ':'); %,'FontSize', 0.5);
% set(axes2,'CLim',[-0.790076280063972 1],'Layer','top',...
%     'TickLabelInterpreter','none','XTick',...
%     [1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22],'XTickLabel',...
%     {'words','uppercases','questions','exclamations','authors','citations',...
%     'firstperson','secondperson','thirdperson','sentencelength','repetitiveness',...
%     'authorHits','titleUppercase','errorLevel','sentiment','informativ',...
%     'superlativesPerWords ','superlativesPerAdjectives'},...
%     'XTickLabelRotation',90,'YTick',[1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22],...
%     'YTickLabel',...
%     {'words','uppercases','questions','exclamations','authors','citations',...
%     'firstperson','secondperson','thirdperson','sentencelength','repetitiveness',...
%     'authorHits','titleUppercase','errorLevel','sentiment','informativ',...
%     'superlativesPerWords ','superlativesPerAdjectives',....
%     'usedsources', 'internsources','externsources','usedimages'});
% title('P-Werte der Korrelationstabelle');
% %matlab2tikz('Abbildungen/KorrelationstabellePValues.tex');
% print -dpdf Abbildungen/KorrelationstabellePValues.pdf;
