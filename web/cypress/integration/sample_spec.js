describe('My First Test', () => {
    it('Does not do much!', () => {
        expect(true).to.equal(true)
    })
})

describe('My first Test', () => {
    it('Does not do much', () => {
        expect(true).to.equal(true)
    })
})

describe('My First Test', ()=> {
    it('Visits the Kitchen Sink', ()=> {
        cy.visit('https://example.cypress.io')

        cy.pause()

        // 找到 type 这个单词，点击这个单词所在的连接
        cy.contains('type').click()
        // 跳转的连接里面应该包含 /commands/actions
        cy.url().should('include', '/commands/actions')
        // 根据css class选择元素
        cy.get('.action-email')
            // 在选择的元素上输入 文本
            .type('fake@email.com')
            // 检查输入的文本是否正确
            .should('have.value', 'fake@email.com')
    })
})