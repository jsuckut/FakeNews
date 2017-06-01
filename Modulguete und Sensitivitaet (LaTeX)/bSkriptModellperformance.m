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
mData = xlsread('ModelleGuete.xlsx');


%% Creat the Boxplot for each Modell
% The method has tow benifits. Number one ist the directly and fixided
% showing of each modelperformance
% Second ist that the boxplot shows the strength of each model
% (sensitivtitat).

figure;
boxplot(mData(:,2), mData(:,1))
title('Trefferquote und Modellrobusheit');
grid on;
ylabel('Trefferqueto');
xlabel('Modelle');
xticklabels({'LRM','SVM','ANN','Tree','LPT',});

matlab2tikz('Abbildungen/ModelleGueteUndRobustheit.tex');
