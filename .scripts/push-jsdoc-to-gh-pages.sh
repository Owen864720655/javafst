#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "belambert/javafst" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing javadoc...\n"
  mvn javadoc:javadoc
  DOCS=$PWD/target/site/apidocs

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/belambert/javafst gh-pages > /dev/null

  cd gh-pages
  git rm -rf *
  cp -arf $DOCS/. .
  git add -f .
  git commit -m "Latest jsdoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null

  echo -e "Published Javadoc to gh-pages.\n"
  
fi
