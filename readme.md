# Ownership Checker

Lightweight decision ownership & governance checker based on an Engineering Governance & AI Readiness model.

This tool helps teams quickly clarify **who owns a decision**, **at what level**, and **what guardrails apply**, especially in AI-accelerated environments.

---

## What it does

- Classifies decisions into:
    - Strategic
    - Architectural
    - Delivery
    - Operational
- Recommends a clear owner or ownership model
- Highlights risk when ownership is unclear
- Defines explicit **AI vs human responsibility guardrails**
- Generates a copy-ready **Decision Ownership Card** suitable for Confluence, Notion, or Jira

---

## Why it exists

Many failures are not caused by bad ideas, but by **unclear decision ownership**.

As AI accelerates analysis and execution, the risk of blurred accountability increases.  
This tool operationalizes a simple principle:

> **AI can inform decisions. Humans must own responsibility.**

---

## How it works

- Uses transparent, deterministic rules (no black-box scoring)
- Asks a small set of classification questions (reversibility, blast radius, time horizon, etc.)
- Maps decisions to ownership expectations defined in an engineering governance model
- Optionally surfaces a short **“reality check”** when ownership smells are detected

This is **decision support**, not decision automation.

---

## Tech stack

- Java 21
- Spring Boot
- Thymeleaf
- No database (stateless MVP)
- Designed as an internal-tool style web app

---

## Governance model

The decision domains, ownership expectations, and AI guardrails are derived from an  
**Engineering Governance & AI Readiness playbook**.

This repository provides a practical interface for applying that model to real decisions.

---

## Status

This is an early MVP intended to:
- Validate usefulness
- Support internal decision hygiene
- Act as a practical companion to the governance playbook

---

## Philosophy

Decision support, not decision replacement.
