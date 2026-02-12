# 대용량 트래픽 이커머스 프로젝트

## 📌 프로젝트 개요

대용량 동시 트래픽을 처리하는 이커머스 플랫폼 백엔드 서비스입니다. 선착순 쿠폰 발급, 인기상품 조회 등 높은 동시성을 요구하는 핵심 기능들을 안정적으로 처리하도록 설계되었습니다.

### 주요 특징
- ⚡ **대용량 트래픽 처리**: 동시성 제어 및 최적화를 통한 안정적인 처리
- 🎟️ **선착순 쿠폰 발급**: 동시성 이슈를 해결한 효율적인 쿠폰 발급 시스템
- 🏆 **인기상품 조회**: 실시간 순위 조회 및 캐싱 전략
- 📨 **이벤트 기반 아키텍처**: Kafka를 활용한 비동기 메시징
- 📊 **성능 최적화**: 부하 테스트 기반의 지속적인 개선

---

## 💼 E-커머스 서비스 설계 문서
- [1. 요구사항명세서](/docs/1.Requireements.md)
- [2. ERD](/docs/2.ERD.md)
- [2-1 DDL](/src/main/resources/schema.sql)
- [3. 시퀀스 다이어그램](/docs/3.SequenceDiagram.md)
- [4. API명세](/docs/4.APISpec.png)

---

## ✏️ 기술 보고서

### 🚀 성능 및 확장성
- [동시성 이슈 분석 및 해결](/docs/report/ConcurrencyReport.md) - 동시성 문제 식별 및 해결 방안
- [부하 테스트 계획서](/docs/report/load_test_plan.md) - 성능 테스트 전략 및 계획
- [부하 테스트 결과 보고서](/docs/report/load_test_report.md) - 부하 테스트 결과 분석

### 🏗️ 아키텍처 및 메시징
- [이벤트 기반 아키텍처 설계](/docs/report/EventDrivenArchitecture.md) - 비동기 메시징 시스템 설계
- [Kafka 활용 및 설정](/docs/report/Kafka.md) - Kafka 메시징 플랫폼 구성

### 🎯 핵심 기능 설계
- [선착순 쿠폰 발급 설계/개발](/docs/report/issued_coupon.md) - 쿠폰 발급 시스템 설계 및 구현
- [쿠폰 발급 카프카 개선](/docs/report/쿠폰_발급_카프카_개선.md) - Kafka 기반 쿠폰 발급 성능 개선
- [인기상품 순위 설계/개발](/docs/report/product_ranking.md) - 실시간 인기상품 순위 시스템