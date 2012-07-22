#!/bin/bash -x

scdir=`dirname $0`

java -cp $scdir/:$scdir/jar/* Analysis.AnalysisRunnerLRT2Recompute $*
