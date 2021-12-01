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

        cy.contains('type').click()
        cy.url().should('include', '/commands/actions')
    })
})