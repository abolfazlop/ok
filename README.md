# AutoPass X

An LSPosed compatibility companion for Bitwarden on Android.

## Current implementation

- Modern LibXposed API 102 entry point.
- Conservative Autofill hint enrichment for standard editable Android text fields.
- Infers only obvious username, email, and password field types from input type and visible hint labels.
- Preserves hints already supplied by the app.
- Respects Android Autofill opt-outs.
- Does not read typed field contents, passwords, clipboard data, or Bitwarden vault data.
- Does not replace Bitwarden's Autofill service or keep the Bitwarden UI process permanently alive.

## Why this can help

Android's Autofill framework sends a structured view tree to the selected Autofill service. Explicit autofillHints help the service recognize login fields. AutoPass X adds a hint only when a standard editable TextView is clearly identifiable and the app has not supplied its own hint.

## Scope

Recommended scope includes Bitwarden and several common browsers. staticScope=false allows the user to add other apps in LSPosed Manager. For wider app coverage, enable AutoPass X for the apps where login fields should be improved. Do not enable it for system_server or System UI.

## Background behavior

Android binds the selected Autofill service when an Autofill request occurs and may start its process if needed. The service is intentionally not required to keep a permanent background process alive. AutoPass X does not bypass Bitwarden's vault lock, master password, or biometric confirmation.

## Known limitations

- This first compatibility hook only covers standard editable Android text views.
- Jetpack Compose, custom canvas fields, virtual WebView fields, and apps that opt out of Autofill need separate compatibility work.
- A save/update prompt after a successful login cannot be guaranteed in arbitrary apps; it depends on Android's save flow and Bitwarden's own service implementation.
- Matching still depends on Bitwarden's vault URI associations and its Autofill configuration.

## Build

GitHub Actions builds a debug APK with Java 17 and Gradle 8.9. The build artifact is published on successful workflow completion.
