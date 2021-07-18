# Bisq 2 Glossary

## Fundamentals

### Bisq

A peer-to-peer [Asset](#asset) [Exchange](#exchange) system

### Asset

Property divisible into fungible [Units](#unit).

> **Gold**, the **US dollar** and **bitcoin** are commonly held _assets_

### Asset Code

A short, unique series of characters used to identify an [Asset](#asset)

> **XAU**, **USD** and **BTC** are the _asset codes_ for gold, the US dollar and bitcoin

Also known as _[Currency](#currency) Code_

<details>
Where a given [Asset](#asset) is a recognized [Currency](#currency), its asset code should correspond with its ISO 4217 currency code value.
</details>

### Currency

An [Asset](#asset) used as a medium of exchange in a given region

> The **bolívar** is Venezuela's official _currency_

### Nominal Asset

An abstract representation of an [Asset](#asset) inclusive of all its [Real](#real-asset) forms but not transferable on its own

> **USD**, **USDT**, **USDC** and **BTC** are _nominal assets_

### Real Asset

A digital or physical manifestation of an [Asset](#asset) that is transferable and therefore suitable for [Settlement](#settlement)

> **Federal reserve notes** (physical cash), **bank deposits**, **L-USDT**, **ERC20-USDT** and **ERC20-USDC** are all _real assets_ suitable for settling USD contracts. **Mainchain BTC**, **Lightning BTC**, **L-BTC** and **WBTC** are all _real assets_ suitable for settling BTC contracts.

### Unit

A named quantity of an [Asset](#asset)

> US dollar _units_ include the **cent** and the **dollar**. Bitcoin _units_ include **sats** and **bitcoin**.

### Base Unit

The smallest transferable [Unit](#unit) of an [Asset](#asset)

> The _base unit_ of the US dollar is the **cent**. The _base unit_ of bitcoin is the **sat**.

### Derived Unit

A named quantity of [Base Units](#base-unit)

> The **dollar** is a _derived unit_ representing one hundred cents. The **bitcoin** is a _derived unit_ representing one hundred million sats.

### Standard Unit

The [Unit](#unit) most commonly used when quantifying an [Asset](#asset)

> Today, the _standard unit_ for bitcoin is **bitcoin**, as in "she saved up 6.15 **bitcoin**". Someday the standard unit for bitcoin may change to the **sat**.

### Amount

A number of [Units](#unit)

> The _amount_ transferred was **6.15 bitcoin**.

### Exchange

To give one [Amount](#amount) and receive another

> Alice _exchanged_ **1 bitcoin** with Bob for **42,000 US dollars**

### Contract

An agreement between [Parties](#party) to [Exchange](#exchange) according to a set of [Terms](#terms)

_See [Contract Types](#contract-types)_

### Party

A person or group of people forming one side of a [Contract](#contract)

> **Alice** and **Bob** were _parties_ to the contract

Also known as _Counterparty_

### Terms

Requirements stipulating:
- [Conditions](#condition) under which [Exchange](#exchange) will occur
- [Amounts](#amount) to be exchanged
- [Security](#security-method), [Settlement](#settlement-method) and [Adjudication](#adjudication-method) methods to be used

### Condition

An objective state that must be realized

> Conditions may include:
> - the passage of a certain amount of time
> - a fact reported on a given date,
> - settlement account being older than a given age

### Offer

A proposal to enter into a [Contract](#contract)

### Maker

A [Party](#party) who creates an [Offer](#offer)

### Taker

A [Party](#party) who accepts an [Offer](#offer)

### Security Method

The means used to keep [Assets](#asset) safe during [Contract](#contract) [Execution](#execution)

### Settlement Method

The means used to transfer ownership of [Assets](#asset)

### Settlement Account

User-specific details for a given settlement method

Notes:
 - Formerly known as _Payment Account_
 - Now necessary for both base account and counter account

### Adjudication Method

The means used to resolve problems and disputes during [Contract](#contract) [Execution](#execution)

### Base Asset

aka _Base Currency_

### Counter Asset

aka _Counter Currency_


## Contract Lifecycle

### Proposal

### Negotiation

### Execution

The process of carrying out the [Terms](#terms) of a [Contract](#contract) after [Acceptance](#acceptance) through to [Conclusion](#conclusion)

### Acceptance

### Fulfillment

### Settlement

#### Settlement Period

### Termination

### Adjudication

### Enforcement

### Completion

### Conclusion


## Contract Types

### Spot Trade

A [Contract](#contract) for immediate [Fulfilment](#fulfillment)

### Forward Trade

A [Contract](#contract) for obligatory [Fulfilment](#fulfillment) on a future date

### Option

A [Contract](#contract) for optional [Fulfilment](#fulfillment) on (or possibly before) a future date

<details>
Notes:
- Advantage for the maker is the premium realized by the strike price
- Requires additional transaction for taker to buy the option itself at its given strike price
- Cancellable without penalty (because premium has already been paid)
- https://www.investopedia.com/articles/active-trading/112213/getting-handle-options-premium.asp
- https://money.stackexchange.com/questions/81612/why-would-someone-want-to-sell-call-options#81616
</details>

#### Call Option

An [Option](#option) to _buy_ the underlying [Asset](#asset)

#### Put Option

An [Option](#option) to _sell_ the underlying [Asset](#asset)

### Loan

A [Contract](#contract) in which a lender gives a borrower [Principal](#principal) to be returned with [Interest](#interest)

> Bob took a _loan_ from Alice for **100,000 USDT repayable in 12 months with 8% APY interest**

<details>
- May involve an additional up-front "loan origination" fee to protect lender against early (i.e. no- or low-interest) payback
- Secured loans would include over-collateralized loans
  - would require 3rd key and forced liquidation / margin call schedule
- Unsecured loans possible. Just as any exchange could be unsecured.
</details>

#### Principal

An [Amount](#amount) representing an investment

#### Interest

An [Amount](#amount) representing the time value of [Principal](#principal) used

### Bet

A [Contract](#contract) in which two [Parties](#party) agree to give the other an [Amount](#amount) and receive nothing in return based on a [Fact](#fact) in the future.

> Alice _bet_ Bob **50,000 sats** that the **Giants would win by 10 points tomorrow night**

#### Fact

A thing that is known or proven to be true

> It is a _fact_ that the score of last night's game was 31-13

#### Oracle

A [Fact](#fact) reporter

### Contract for Difference (CFD)

A [Bet](#bet) where the [Amount](#amount) is based on the difference between the current and future value of an [Asset](#asset)

