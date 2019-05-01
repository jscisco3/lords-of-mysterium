## Feature Roadmap


#### Dungeon Crawling
- Dungeon Generation
    - Terrain Generation
    - Stair Generation
- Dungeon Exploration
    - Fog of War
    
        Fog of war should be used as a representation of how the dungeon has been explored.
        
        A dungeon starts with no tiles revealed.
        
        As a tile is revealed, we are still able to _remember_ what entities we had seen there, though we will not
        be aware if they have moved since we last saw them.
        
    - Field of View
        - Field of View Attribute
        
            This is pretty straightforward. It will track the radius of how far an entity can see.
            
            For a player, this will impact the Fog Of War.
        
    - Handling Turns
    - Handling AI
    - Handling Player Input
- Dungeon Saving
- Dungeon Loading
- Entity Generation
    - Basic stats
- Entity Saving
- Entity Loading
- Data Driven Approach
- Entity Interactions
    - Item interactions
        - Inventory Component
        - Inventory Facets
            - Pick Item Up
            - Drop Item
    - Equipment interactions
        - Equipment Component
        - Equipment Facets
            - Equip Item
            - Unequip Item
    - Combat interactions
        - Melee
        - Ranged
        - Stats
    - Magic interactions
        - Damage Spells
        - Buff Spells
- Timed effects
- Permanent effects
- Triggered events

#### Kingdom
- Recruit heroes
- Store found items

#### Configuration
- Read configurations from external file
    - Game related configurations
    - Dungeon related configurations
    - Kingdom related configurations