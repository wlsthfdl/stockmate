https://app.notion.com/p/OMS-Inventory-39d228f6ff9e8038b19af5b1a443b1a7


### 주문 동시 취소 시 재고 이중 복구 방지
같은 주문에 대한 동시 취소 요청 10건 → 락 없이 재고가 10번 복구(5 → 23)되는 문제를 테스트로 재현하고,
주문 행 비관적 락으로 1건만 성공하도록 해결.
→ [상세 과정 (Notion)](https://app.notion.com/p/OMS-Inventory-39d228f6ff9e8038b19af5b1a443b1a7)