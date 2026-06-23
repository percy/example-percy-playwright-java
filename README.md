# example-percy-playwright-java
Example demonstrating Percy's Java Playwright integration.

## Java Playwright Tutorial

The tutorial assumes you're already familiar with Java and Playwright and focuses on using it with Percy. You'll still be able to follow along if you're not familiar with Java, Playwright, but we won't spend time introducing Java, Playwright concepts.

### Prerequisites

TestNG v7.11+, Java v20+, Playwright v1.55+

If you are using CLI for running tests, ensure that Maven is installed on your machine, its environment variables are set, and its bin is added to system path, $PATH


### Step 1

Clone the example application and install dependencies:

Minimum required `@percy/cli` version is `1.31.2` for this to work correctly. If you already have `@percy/cli` or `@percy/webdriver-utils` installed please update it to latest or minimum required version.

```bash
$ git clone git@github.com:percy/example-percy-playwright-java
$ cd example-percy-playwright-java
$ mvn compile
$ npm install
$ npx playwright install
```

This repo has examples for both **Percy Web** and **Percy on Automate**.

**Note:** For Percy Web, a local browser is used. You don't need the BrowserStack Automate setup for this. You can skip straight to Step 2.

For Percy on Automate, you will need BrowserStack credentials. Refer to following instructions to get the same.

1. You will need a BrowserStack `username` and `access key`. To obtain your access credentials, [sign up](https://www.browserstack.com/users/sign_up?utm_campaign=Search-Brand-India&utm_source=google&utm_medium=cpc&utm_content=609922405128&utm_term=browserstack) for a free trial or [purchase a plan](https://www.browserstack.com/pricing).

2. Please get your `username` and `access key` from [profile](https://www.browserstack.com/accounts/profile) page.

### Step 2

Sign in to Percy and create a new project. You can name the project "test-project" if you'd like. After you've created the project, you'll be shown a token environment variable.

**Note:** For Percy Web, create a **Web** project. The token will start with ***web_*** keyword.

**Note:** For Percy on Automate, create an **Automate** project under **Web** category. The token will start with ***auto_*** keyword.

### Step 3

In the shell window you're working in, export the token and other environment variables:

**Unix**

``` shell
$ export PERCY_TOKEN="<your token here>"
```

**Windows**

``` shell
$ set PERCY_TOKEN="<your token here>"

# PowerShell
$ $Env:PERCY_TOKEN="<your token here>"
```

Set the necessary **BROWSERSTACK ENVIRONMENT** variables, **only required for Percy on Automate examples**

**Unix**

``` shell
$ export BROWSERSTACK_USERNAME="<your browserstack user_name>"
$ export BROWSERSTACK_ACCESS_KEY="<your browserstack access_key>"
```

**Windows**

``` shell
$ set BROWSERSTACK_USERNAME="<your browserstack user_name>"
$ set BROWSERSTACK_ACCESS_KEY="<your browserstack access_key>"

# PowerShell
$ $Env:BROWSERSTACK_USERNAME="<your browserstack user_name>"
$ $Env:BROWSERSTACK_ACCESS_KEY="<your browserstack access_key>"
```

Alternatively you can also update `USERNAME`, `AUTOMATE_KEY` with Browserstack User name, Access key in the script as well.

### Step 4: Generate first Percy build (baseline)

Run the tests to create your first Percy build. This serves as the baseline for visual comparisons.

#### For Percy Web

``` shell
$ npx percy exec --verbose  --  mvn test -P web-percy-test
```

#### For Percy on Automate

``` shell
$ npx percy exec --verbose  --  mvn test -P automate-percy-test
```

Since this is the first build, all snapshots will be new and there will be no comparisons yet.

### Step 5: Generate second Percy build (with visual changes)

Run the "after" tests which contain pre-made visual changes (selects a Samsung product instead of Apple). This build will be compared against the baseline to surface visual diffs.

#### For Percy Web

``` shell
$ npx percy exec --verbose  --  mvn test -P web-percy-after-test
```

#### For Percy on Automate

``` shell
$ npx percy exec --verbose  --  mvn test -P automate-percy-after-test
```

On completion, visit your Percy dashboard to see the visual changes detected between the two builds.

### Finished! 😀

From here, you can try making your own changes to the website and functional tests, if you like. If you do, re-run
the tests and you'll see any visual changes reflected in Percy.
