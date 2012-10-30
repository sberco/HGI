#!/bin/bash -x

if [ -z $RUNNER ]; then
    RUNNER="Analysis.AnalysisRunnerLRT2Recompute"
fi

scdir=`dirname $0`

java -cp $scdir/:$scdir/jar/* $RUNNER $*
