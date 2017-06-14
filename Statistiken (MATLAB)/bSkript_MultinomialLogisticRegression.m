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
% version 2017-06-06

clear; clc; close all;
addpath('Funktionsbibliothek/MATLAB2Tikiz/src');
addpath('Funktionsbibliothek');

%% Import the actual data
% Loading the data from sql Database csv export
% Each vector in the matrix stands for the input:
% 1 = 'newsId'
% 2 = 'isFake'
% 3 = 'words'
% 4 = 'uppercases'
% 5 = 'questions'
% 6 = 'exclamations'
% 7 = 'authors'
% 8 = 'citations'
% 9 = 'firstperson'
% 10 = 'secondperson'
% 11 = 'thirdperson'
% 12 = 'sentencelength'
% 13 = 'repetitiveness'
% 14 = 'authorHits'
% 15 = 'titleUppercase'
% 16 = 'errorLevel'
% 17 = 'sentiment'
% 18 = 'informativeness'
sVar = {'newsId','isFake','words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','titleUppercase','errorLevel','sentiment','informativeness'};
mData = csvread('Datenbank/2017-06-05newsResults.csv');

%% Sort the data for regression
% Sorts the data according to dependent (Y) and independent variables (X)
vY = mData(:,2);
mX = mData(:,3:end);

%% Correlation coefficients
% [r, p] = corrcoef(x) Korrelationskoeffiziente und p-Werte berechnen 
sNamesCorrelation = {'words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','titleUppercase','errorLevel','sentiment','informativeness'};
[mR, mP] = corrcoef(mX);

    
%% Calculate the model
% Use the function for the model - Old Version: 
% @source: https://de.mathworks.com/help/stats/generalizedlinearmodel-class.html?searchHighlight=GeneralizedLinearModel&s_tid=doc_srchtitle
cLogistischeRegression  = GeneralizedLinearModel.fit(mX,vY,'distr','binomial');

%% Calculate the 
% source: http://bit.csc.lsu.edu/~jianhua/emrah.pdf
% @index: at 2:end, because B1 is the constant.
vBeta = table2array(cLogistischeRegression.Coefficients(2:end,1));
vOddRatio = exp(vBeta);
dlmwrite('Datenexporte/LogistischeRegressionOddRatio.csv',vOddRatio,'delimiter',',','precision','%.2f');

%% Plot
figure;
surf(cLogistischeRegression.CoefficientCovariance);
title('Koeffizienten Kovarianz');
grid 'on';
xlabel('Variablen');
ylabel('Variablen');
view([-17 34]); % view adjust.
% Sava Data as TikZ
matlab2tikz('Abbildungen/CoefficientCovarianceLogisticRegression.tex');
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
dlmwrite('Datenexporte/LogistischeRegressionCoefficients.csv',mCoefficients,'delimiter',',','precision','%.5f');


%% Export anthordes Values
% Save the informations as MATLAB-TABLE Object
tcorrcoef = table(mR(:,1), mR(:,2), mR(:,3), mR(:,4), mR(:,5), mR(:,6), mR(:,7),...
    mR(:,8), mR(:,9), mR(:,10), mR(:,11), mR(:,12),  mR(:,13), mR(:,14), mR(:,15),...
    mR(:,16),'RowNames',sNamesCorrelation, 'VariableNames',{'words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','titleUppercase','errorLevel','sentiment','informativeness'});
% Save the Data @code 'WriteRowNames',1 right down the Values
writetable(tcorrcoef,'Datenexporte/tcorrcoef.csv','WriteRowNames',1);
writetable(tcorrcoef,'Datenexporte/tcorrcoef.xls','WriteRowNames',1);

% Save the informations as MATLAB-TABLE Object
tcorrcoef = table(mP(:,1), mP(:,2), mP(:,3), mP(:,4), mP(:,5), mP(:,6), mP(:,7),...
    mP(:,8), mP(:,9), mP(:,10), mP(:,11), mP(:,12),  mP(:,13), mP(:,14), mP(:,15),...
    mP(:,16),'RowNames',sNamesCorrelation, 'VariableNames',{'words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','titleUppercase','errorLevel','sentiment','informativeness'});
% Save the Data
writetable(tcorrcoef,'Datenexporte/tcorrcoefPValues.csv','WriteRowNames',1);
writetable(tcorrcoef,'Datenexporte/tcorrcoefPValues.xls','WriteRowNames',1);

figure1 = figure;
axes1 = axes('Parent',figure1);
hold(axes1,'on');
heatmap(mR, sNamesCorrelation, sNamesCorrelation, '%0.5f'); %'TickAngle', 90, 'GridLines', ':'); %,'FontSize', 0.5);
  set(axes1,'CLim',[-0.790076280063972 1],'Layer','top',...
    'TickLabelInterpreter','none','XTick',...
    [1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16],'XTickLabel',...
    {'words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','titleUppercase','errorLevel','sentiment','informativ'},...
    'XTickLabelRotation',90,'YTick',[1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16],...
    'YTickLabel',...
    {'words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','titleUppercase','errorLevel','sentiment','informativ'});
title('Korrelationstabelle');
%matlab2tikz('Abbildungen/Korrelationstabelle.tex');
% print -dpdf Abbildungen/Korrelationstabelle.pdf;


figure2 = figure;
axes2 = axes('Parent',figure2);
hold(axes1,'on');
heatmap(mP, sNamesCorrelation, sNamesCorrelation, '%0.4f'); %'TickAngle', 90, 'GridLines', ':'); %,'FontSize', 0.5);
  set(axes2,'CLim',[-0.790076280063972 1],'Layer','top',...
    'TickLabelInterpreter','none','XTick',...
    [1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16],'XTickLabel',...
    {'words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','titleUppercase','errorLevel','sentiment','informativ'},...
    'XTickLabelRotation',90,'YTick',[1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16],...
    'YTickLabel',...
    {'words','uppercases','questions','exclamations','authors','citations','firstperson','secondperson','thirdperson','sentencelength','repetitiveness','authorHits','titleUppercase','errorLevel','sentiment','informativ'});
title('Korrelationstabelle');
%matlab2tikz('Abbildungen/KorrelationstabellePValues.tex');
% print -dpdf Abbildungen/KorrelationstabellePValues.pdf;
