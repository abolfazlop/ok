# AutoPass X

LSPosed companion module for Bitwarden on Android.

> Status: early development scaffold. This repository does **not** yet claim universal autofill, save-password detection, or background vault unlocking.

## Goal
Improve Android login-field compatibility while keeping Bitwarden as the only vault and preserving Android's security model.

## Important constraints
- Android binds an AutofillService only when the user explicitly selects it in system settings.
- AutoPass X does not replace Bitwarden's AutofillService, read vault contents, bypass the vault lock, or synthesize credentials.
- Android apps can opt out of Autofill or use custom fields that cannot be generically recognized.
- A module cannot reliably detect successful logins in every third-party app.

## Build
The GitHub Actions workflow builds a debug APK using Gradle and Java 17.

## Device target
POCO X7 Pro / Android 16 / KernelSU / LSPosed. Compatibility still needs on-device testing.

## Safety
Do not grant this module network access. Do not store passwords, usernames, vault data, or authentication tokens in this module.
