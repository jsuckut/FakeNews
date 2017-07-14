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



%% Chi-Qudrat-Unabhaenigkeits-Test 
%@code: chi2gof
% In Abfolge der Modelle
[hLR,pLR,statsLR] = chi2gof(vLogisticRegressionValues);
[hE,pE,statsE] = chi2gof(vEntscheidungsbaeumeValues);
[hNB,pNB,statsNB] = chi2gof(vNaiveBayesValues);
[hSVM,pSVM,statsSVM] = chi2gof(vSVMValues);


figure;
histogram(vLogisticRegressionValues, 'FaceAlpha', 0.1, 'FaceColor',[0 0 0]);
hold on; 
histogram(vEntscheidungsbaeumeValues,'FaceAlpha', 0.2, 'FaceColor',[0 0 0]);
hold on;
histogram(vNaiveBayesValues,'FaceAlpha', 0.8, 'FaceColor',[0 0 1]);
hold on;
histogram(vSVMValues,'FaceAlpha', 0.4, 'FaceColor',[0 0 0]);
legend('Logistich Regression','Entscheidungsbaum','Naive Bayes','SVM');
grid on;
title('Verteilung der Ergebnisse');
matlab2tikz('Abbildungen/ChiQudratGegenueberstellung.tex');
