%% bSkriptModellperformance
% This skript realse the visualisation of performance
% @Author: Benjamin M. Abdel-Karim
% @since: 2017-05-30
% @version: 2017-05-31

%% Clear everthing
clear; clc; close all;

%% Korrekten Pfad zu den Unterfunktionen legen
addpath('Funktionsbibliothek/MATLAB2Tikiz/src');

%% Import the Data
% Load the Data in a new matrix
%mData = xlsread('ModelleGuete.xlsx');

% Logistic Regression
vLogisticRegressionValues = csvread('Datenbank/LogisticRegression.csv');
vLogisticRegressionID = zeros(length(vLogisticRegressionValues),1);
vLogisticRegressionID = vLogisticRegressionID+1; % Index 1.

% Entscheidungsbaeume
vEntscheidungsbaeumeValues = csvread('Datenbank/DecisionTree.csv');
vEntscheidungsbaeumeID = zeros(length(vEntscheidungsbaeumeValues),1);
vEntscheidungsbaeumeID = vEntscheidungsbaeumeID+2; % Index 2.


% Die NaiveBayes
% Jeder Modellwert enthaelt eine ID von 1 bis n. Messung erfolgt ueber die
% Laenge der Eintrage. Wird fuer den Boxplot benotigt.
vNaiveBayesValues=  csvread('Datenbank/NaiveBayes.csv');
vNaiveBayesID = zeros(length(vNaiveBayesValues),1);
vNaiveBayesID = vNaiveBayesID+3; % Index 3. 

% SVM
vSVMValues =  csvread('Datenbank/SVM.csv');
vSVMValuesID = zeros(length(vSVMValues),1);
vSVMValuesID = vSVMValuesID+4; % Index 4.



%% Setze das Ergebnisfeld zusammen
% Das Ergebnisfeld fuer die Modelle wird zusammen gesetzt. 
mData = [vLogisticRegressionValues, vLogisticRegressionID
     vEntscheidungsbaeumeValues, vEntscheidungsbaeumeID;
vNaiveBayesValues, vNaiveBayesID;
        vSVMValues, vSVMValuesID;];


%% Creat the Boxplot for each Modell
% The method has tow benifits. Number one ist the directly and fixided
% showing of each modelperformance
% Second ist that the boxplot shows the strength of each model
% (sensitivtitat).

figure;
boxplot(mData(:,1), mData(:,2))
title('Trefferquote und Modellrobusheit');
grid on;
ylabel('Trefferqueto');
xlabel('Modelle');
xticklabels({'Logistic Regression','Entscheidungsbaum', 'Naive Bayes', 'SVM' });
% matlab2tikz('Abbildungen/ModelleGueteUndRobustheit.tex');



%% Chi-Qudrat-Unabhaenigkeits-Test 
%@code: chi2gof
% In Abfolge der Modelle
[hLR,pLR,statsLR] = chi2gof(vLogisticRegressionValues);
[hE,pE,statsE] = chi2gof(vEntscheidungsbaeumeValues);
[hNB,pNB,statsNB] = chi2gof(vNaiveBayesValues);
[hSVM,pSVM,statsSVM] = chi2gof(vSVMValues);
