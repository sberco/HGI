#!/bin/bash

pwd=`pwd`

java -cp $pwd/:$pwd/jar/* Analysis.AnalysisRunnerLRT2Recompute $*
